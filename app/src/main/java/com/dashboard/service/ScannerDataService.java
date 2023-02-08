package com.dashboard.service;
import com.dashboard.config.AppProperties;
import com.dashboard.model.ScannerModel;
import com.dashboard.model.ScannerType;
import com.dashboard.model.UnifiedDataScanner;
import com.dashboard.service.ifc.DataDriverHandlerServiceIfc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.InternalException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Service
@Slf4j
public class ScannerDataService {
    @Autowired
    List<DataDriverHandlerServiceIfc> scanners;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UnifiedDataService unifiedDataService;
    @Autowired
    MongoTemplate mongoTemplate;

    @SneakyThrows
    public void uploadNewScannerFile(MultipartFile scannerDataFile, String productName, String microserviceName, String branchVersion, String scannerName, String testName, String buildNumber) {
        log.debug("uploadNewScannerFile process is starting. Details: {}, {}, {}", productName, microserviceName, buildNumber);
        UnifiedDataScanner unifiedData = UnifiedDataScanner.builder()
                .productName(productName)
                .testName(testName)
                .microserviceName(microserviceName)
                .version(branchVersion)
                .buildNumber(buildNumber)
                .scannerName(scannerName)
                .build();
        if (appProperties.getMapScannerToUrl().containsKey(scannerName)) {
            log.info("dynamic scanner " + scannerName);
            unifiedData = callExternalScannerHandler(scannerName, scannerDataFile, unifiedData);
        } else if (scanners.stream().anyMatch(scanner -> scanner.getScannerType().toString().equals(scannerName))) {
            log.info("scanner " + scannerName + " start parsing process");
            DataDriverHandlerServiceIfc dataDriverHandlerService = (scanners.stream().filter(scanner -> scanner.getScannerType().toString().equals(scannerName))).toList().get(0);
            dataDriverHandlerService.handleAndParsingData(scannerDataFile, unifiedData);
        } else {
            log.error("no such scanner find with name " + scannerName);
            throw new Exception("no such scanner find with name " + scannerName);
        }
        unifiedDataService.addUnifiedData(unifiedData);
    }

    public UnifiedDataScanner callExternalScannerHandler(String scannerName, MultipartFile scannerDataFile, UnifiedDataScanner unifiedData) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(appProperties.getMapScannerToUrl().get(scannerName));
        try {
            String unifiedRequestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(unifiedData);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("unifiedData" ,unifiedRequestBody,ContentType.APPLICATION_JSON);
            builder.addBinaryBody(
                    "file",
                    scannerDataFile.getInputStream(),
                    ContentType.MULTIPART_FORM_DATA,
                    scannerDataFile.getName()
            );
            uploadFile.setEntity(builder.build());
            CloseableHttpResponse response = httpClient.execute(uploadFile);
            log.info("dynamic scanner " + scannerName + "try to connect for parsing");
                if(HttpStatus.OK.value() == response.getStatusLine().getStatusCode())
                unifiedData= objectMapper.readValue( EntityUtils.toString(response.getEntity()),UnifiedDataScanner.class);
                else
                    throw new InternalException("internal exception");
        } catch (IOException  | InternalException  e) {
            log.error("IOException|InterruptedException: {},{} return a null response from request component", e.getMessage(), e);
            throw new RuntimeException(e + "return a null response from request component");
        }
        return unifiedData;
    }


    public HashMap<String, List<ScannerModel>> getAllScannersData(String productName, String serviceName, String branchName, LocalDate fromDate, LocalDate toDate, String scannerType) {
        log.info("query for field filter");
        final Query query = new Query();
        HashMap<String, List<ScannerModel>> scannersGraphsData = new HashMap<>();
        if (branchName != null && branchName.length() != 0)
            query.addCriteria(Criteria.where("version").is(branchName));
        if (serviceName != null && serviceName.length() != 0)
            query.addCriteria(Criteria.where("microserviceName").is(serviceName));
        if (productName != null && productName.length() != 0)
            query.addCriteria(Criteria.where("productName").is(productName));
        if (fromDate != null && toDate != null)
            query.addCriteria(Criteria.where("executionDate").gte(fromDate).lt(toDate));
        if (scannerType != null && scannerType.length() != 0)

            query.addCriteria(Criteria.where("scannerType").is(scannerType));
        query.fields().include("buildNumber").include("scannerName").include("executionDate").include("scannerType").include("numberOfSuccessfulScans").include("numberOfRisksHigh").include("numberOfRisksMedium").include("numberOfRisksLow").include("numberOfWarnings");
        query.with(Sort.by(Sort.Order.desc("executionDate")));
        List<ScannerModel> scannersList = mongoTemplate.find(query, ScannerModel.class);

        log.info("calls function that returns a list of all scanners");
        ArrayList<String> scannerNameArr = getListScannerName();

        for (String myScannerName : scannerNameArr) {
            scannersGraphsData.put(myScannerName,
                    scannersList.stream().filter(scannerModel1 -> scannerModel1.getScannerName().equals(myScannerName)).collect(Collectors.toList()));
        }
        log.info("sum of medium risks and low risks are field warnings");
        for (ScannerModel scannerModel : scannersList) {
            if (scannerModel.getNumberOfRisksMedium() == null)
                scannerModel.setNumberOfRisksMedium(0);
            if (scannerModel.getNumberOfRisksLow() == null)
                scannerModel.setNumberOfRisksLow(0);
            scannerModel.setNumberOfWarnings(scannerModel.getNumberOfRisksMedium() + scannerModel.getNumberOfRisksLow());
        }
        return scannersGraphsData;
    }

    public ArrayList<String> getListScannerName() {
        ArrayList<String> scannerNameArr = new ArrayList<>();
        for (ScannerType myScanner : ScannerType.values()) {
            scannerNameArr.add(myScanner.toString());
        }
        return scannerNameArr;
    }


}
