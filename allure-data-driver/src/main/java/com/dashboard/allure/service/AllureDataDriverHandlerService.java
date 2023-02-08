package com.dashboard.allure.service;

import com.dashboard.Exceptions.ScannerParsingException;
import com.dashboard.Exceptions.ScannerAggregationException;
import com.dashboard.allure.AllureConstant;
import com.dashboard.allure.model.AllureFile;
import com.dashboard.allure.model.AllureGlobalDetails;
import com.dashboard.allure.model.TestDetails;
import com.dashboard.model.*;
import com.dashboard.model.*;
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

import static com.dashboard.model.Status.FAILED;
import static com.dashboard.model.Status.PASSED;

@Component
@Slf4j
public class AllureDataDriverHandlerService implements DataDriverHandlerServiceIfc {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    @SneakyThrows
    public void handleAndParsingData(MultipartFile file, UnifiedDataScanner unifiedData) {
        try {
            AllureFile allureFile =  objectMapper.readValue(file.getInputStream(), AllureFile.class);
            log.info("allure data parsing . file name: {}, Build Number :{} ", file.getOriginalFilename(), unifiedData.getBuildNumber());
            unifiedData = AggregationToUnifiedDataAllureScanner(allureFile, unifiedData);
            unifiedData.setScannerType(this.getScannerGroupType());
            log.info("unifiedData from allure scanner");
        } catch (Exception e) {
            log.error("error on handleAndParsingData" + e.getMessage());
            throw new ScannerParsingException("error on handleAndParsingData", e);
        }
    }
    static int numberOfSuccessfulScans;
    static int numberOfRisksHigh;
    @SneakyThrows
    public UnifiedDataScanner AggregationToUnifiedDataAllureScanner(AllureFile allureFile, UnifiedDataScanner unifiedData) {
        try {
            log.debug("start AggregationToUnifiedDataAllureScanner");
            unifiedData.setExecutionDate(LocalDateTime.now());
            unifiedData.setCustomizedFields(FillInGlobalCustomizedFields(allureFile));
            List<AllureGlobalDetails> allureGlobalDetails = allureFile.getChildren();
            if (allureGlobalDetails != null) {
                List<TestDetails> testDetailsList = GetTestList(allureGlobalDetails);
                if (testDetailsList != null) {
                    unifiedData.setListOfTests(AggregateTestsList(testDetailsList));
                    unifiedData.setNumberOfScans(unifiedData.getListOfTests().size());
                    unifiedData.setNumberOfSuccessfulScans(numberOfSuccessfulScans);
                    unifiedData.setNumberOfRisksHigh(numberOfRisksHigh);
                    log.debug("end AggregationToUnifiedDataAllureScanner");
                }
            }
        } catch (Exception e) {
            log.error("error occur in AggrigationToUnifiedDataAllureScanner" + e.getMessage());
            throw new ScannerAggregationException("error occur in AggrigationToUnifiedDataAllureScanner", e);
        }
        return unifiedData;
    }
    @SneakyThrows
    public List<TestDetails> GetTestList(List<AllureGlobalDetails> allureGlobalDetails) {
        List<TestDetails> testDetailsList = new ArrayList<>();
        try {
            log.info("start GetTestList");
            allureGlobalDetails.forEach(oneAllureGlobalDetails -> {
                if (oneAllureGlobalDetails.getChildren() != null) {
                    oneAllureGlobalDetails.getChildren().forEach(testServise -> {
                        if (testServise.getChildren() != null) {
                            testServise.getChildren().forEach(testClass -> {
                                if (testClass.getChildren() != null) {
                                    testClass.getChildren().forEach(testDetails -> {
                                        if (testDetails != null)
                                            testDetailsList.add(testDetails);
                                    });
                                }
                            });
                        }
                    });
                }
            });
            log.info("end GetTestList");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }
        return testDetailsList;
    }
    @SneakyThrows
    public List<ScannerTest> AggregateTestsList(List<TestDetails> tests){
        try {
            log.info("start AggregateTestsList");
            List<ScannerTest> listOfTestsAfterAggrigation = new ArrayList<>();
            tests.forEach(oneTest -> {
                ScannerTest oneScannerTest = new ScannerTest();
                oneScannerTest.setDescription(oneTest.getName());
                switch (oneTest.getStatus()) {
                    case "passed" : {
                        oneScannerTest.setStatus(PASSED);
                        numberOfSuccessfulScans+=1;
                    }break;
                    case "failed":{
                        oneScannerTest.setStatus(FAILED);
                        numberOfRisksHigh+=1;
                        oneScannerTest.setSeverity(Severity.HIGH);
                    } break;
                }
                oneScannerTest.setId(oneTest.getUid());
                oneScannerTest.setCustomizedFields(FillInOneTestCustomizedFields(oneTest));
                listOfTestsAfterAggrigation.add(oneScannerTest);
            });
            return listOfTestsAfterAggrigation;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }
    }
    @SneakyThrows
    public Map<String, String> FillInGlobalCustomizedFields(AllureFile allureFile){
        try {
            Map<String, String> customizedFields = new HashMap<>();
            customizedFields.put(AllureConstant.GLOBAL_ID, allureFile.getUid());
            customizedFields.put(AllureConstant.GLOBAL_NAME, allureFile.getName());
            return customizedFields;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }

    }
    @SneakyThrows
    public Map<String, String> FillInOneTestCustomizedFields(TestDetails testDetails){
        try {
            Map<String, String> customizedFields = new HashMap<>();
            customizedFields.put(AllureConstant.PARENT_ID,testDetails.getParentUid());
            customizedFields.put(AllureConstant.START_TIME, testDetails.getTime().getStart());
            customizedFields.put(AllureConstant.STOP_TIME, testDetails.getTime().getStop());
            customizedFields.put(AllureConstant.DURATING_TIME, (testDetails.getTime().getDuration()).toString());
            return customizedFields;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage() ,e);
        }

    }
    @Override
    public ScannerType getScannerType() {
        return ScannerType.ALLURE;
    }

    @Override
    public ScannerGroupType getScannerGroupType() { return ScannerGroupType.TEST_REPORT; }
}
