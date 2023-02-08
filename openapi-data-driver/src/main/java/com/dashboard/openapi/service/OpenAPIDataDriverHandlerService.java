package com.dashboard.openapi.service;

import com.dashboard.Exceptions.ScannerAggregationException;
import com.dashboard.Exceptions.ScannerParsingException;
import com.dashboard.model.*;
import com.dashboard.model.*;
import com.dashboard.openapi.OpenApiConstant;
import com.dashboard.openapi.model.ChangedOperation;
import com.dashboard.openapi.model.OpenApiScannerFile;
import com.dashboard.openapi.model.Operation;
import com.dashboard.openapi.model.SpecOpenApi;
import com.dashboard.service.ifc.DataDriverHandlerServiceIfc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@Slf4j
public class OpenAPIDataDriverHandlerService implements DataDriverHandlerServiceIfc {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    @SneakyThrows
    public void handleAndParsingData(MultipartFile file, UnifiedDataScanner unifiedData) {
        try {
        log.debug("start handleAndParsingData");
        OpenApiScannerFile openApiScannerFile = objectMapper.readValue(file.getInputStream(), OpenApiScannerFile.class);
        unifiedData = AggregationToUnifiedDataOpenApi(openApiScannerFile, unifiedData);
        unifiedData.setScannerType(this.getScannerGroupType());
        log.debug("end handleAndParsingData");
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerParsingException("error on handleAndParsingData", e);
        }
    }
    @SneakyThrows
    public UnifiedDataScanner AggregationToUnifiedDataOpenApi(OpenApiScannerFile openApiScannerFile, UnifiedDataScanner unifiedData){
        try {
            log.debug("start AggregationToUnifiedDataOpenApi");
            List<ChangedOperation> Allerrors = GetTestsList(openApiScannerFile);
            List<ScannerTest> listOfTestToPush = AggregateTestsListFromErrorsList(Allerrors);
            unifiedData.setListOfTests(listOfTestToPush);
            unifiedData.setNumberOfRisksHigh(listOfTestToPush.size());
            unifiedData.setNumberOfScans(listOfTestToPush.size());
            unifiedData.setNumberOfSuccessfulScans(0);
            if (openApiScannerFile.getOldSpecOpenApi()!=null){
               unifiedData.setCustomizedFields(aggregationCustomizedFieldsSpecOpenApi(openApiScannerFile.getOldSpecOpenApi()));
            }
            unifiedData.setExecutionDate(LocalDateTime.now());
            log.debug("end AggregationToUnifiedDataOpenApi");
            return unifiedData;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }
    };
    @SneakyThrows
    private List<ScannerTest> AggregateTestsListFromErrorsList(List<ChangedOperation> Allerrors){
        try{
            List<ScannerTest> listOfTestToPush = new ArrayList<>();
            Allerrors.forEach(oneError -> {
                ScannerTest scannerTest = new ScannerTest();
                Operation opaertion = getOperation(oneError);
                if (opaertion != null) {
                    scannerTest.setId(opaertion.getOperationId());
                    scannerTest.setDescription(opaertion.getSummary());
                }
                scannerTest.setStatus(Status.FAILED);
                scannerTest.setSeverity(Severity.HIGH);
                scannerTest.setCustomizedFields(aggregationCustomizedFields(oneError));
                listOfTestToPush.add(scannerTest);
            });
            return listOfTestToPush;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }
    }
    @SneakyThrows
    private Operation getOperation(ChangedOperation changedOperation){
        try {
            if (changedOperation.getOperation()!=null){
                return changedOperation.getOperation();
            }
            else
                return changedOperation.getOldOperation();
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }

    }
    @SneakyThrows
    private List<ChangedOperation> GetTestsList(OpenApiScannerFile openApiScannerFile){
        try {
            log.debug("start GetTestsList");
            List<ChangedOperation> allErrors = new ArrayList<>();
            AddToErrorsList(openApiScannerFile.getNewEndpoints(), allErrors);
            AddToErrorsList(openApiScannerFile.getMissingEndpoints(), allErrors);
            AddToErrorsList(openApiScannerFile.getChangedOperations(), allErrors);
            AddToErrorsList(openApiScannerFile.getChangedSchemas(), allErrors);
            AddToErrorsList(openApiScannerFile.getChangedElements(), allErrors);
            AddToErrorsList(openApiScannerFile.getDeprecatedEndpoints(), allErrors);
            log.debug("end GetTestsList");
            return allErrors;
        }
        catch (Exception e){
             log.error(e.getMessage());
             throw new ScannerAggregationException(e.getMessage(), e);
        }
    }
    @SneakyThrows
    private List<ChangedOperation> AddToErrorsList(List<ChangedOperation> changedOperationList, List<ChangedOperation> allErrors){
        try {
            if (changedOperationList != null) {
                changedOperationList.forEach(oneError -> {
                    if (oneError != null) {
                        allErrors.add(oneError);
                    }
                     });
            }
            return allErrors;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }


    }
    @SneakyThrows
    private Map<String, String> aggregationCustomizedFields(ChangedOperation changedOperation){
        try {
            log.debug("start aggregationCustomizedFields");
            Map<String, String> customizedFields = new HashMap<>();
            Operation operation = getOperation(changedOperation);
            customizedFields.put(OpenApiConstant.PATH_URL, changedOperation.getPathUrl());
            customizedFields.put(OpenApiConstant.HTTP_METHOD, changedOperation.getHttpMethod());
            customizedFields.put(OpenApiConstant.OPERTION_ID, operation.getOperationId());
            customizedFields.put(OpenApiConstant.DESCRIPTION, operation.getSummary());
            log.debug("end aggregationCustomizedFields");
            return customizedFields;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }
    }
    @SneakyThrows
    private Map<String, String> aggregationCustomizedFieldsSpecOpenApi(SpecOpenApi specOpenApi){
        try {
            log.debug("start aggregationCustomizedFieldsSpecOpenApi");
            Map<String, String> customizedFields = new HashMap<>();
            customizedFields.put(OpenApiConstant.OPEN_API, specOpenApi.getOpenapi());
            customizedFields.put(OpenApiConstant.VERSION, specOpenApi.getInfo().getVersion());
            customizedFields.put(OpenApiConstant.TITLE, specOpenApi.getInfo().getTitle());
            log.debug("end aggregationCustomizedFieldsSpecOpenApi");
            return customizedFields;

        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }
    }

    @Override
    public ScannerType getScannerType() {
        return ScannerType.OPENAPI;
    }

    @Override
    public ScannerGroupType getScannerGroupType() { return ScannerGroupType.SECURITY; }
}
