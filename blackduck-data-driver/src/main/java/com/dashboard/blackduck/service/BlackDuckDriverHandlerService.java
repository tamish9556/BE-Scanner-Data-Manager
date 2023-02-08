package com.dashboard.blackduck.service;

import com.dashboard.Exceptions.ScannerParsingException;
import com.dashboard.blackduck.model.*;
import com.dashboard.model.*;
import com.dashboard.Exceptions.ScannerAggregationException;
import com.dashboard.blackduck.constants.BlackDuckConstants;
import com.dashboard.blackduck.model.*;
import com.dashboard.model.*;
import com.dashboard.service.ifc.DataDriverHandlerServiceIfc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BlackDuckDriverHandlerService implements DataDriverHandlerServiceIfc {
    @Value("${BlackDuckUrl.listOfRepos}")
    private String blackDuckUrlListOfRepos;
    JsonObject jsonObject;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleAndParsingData(MultipartFile file, UnifiedDataScanner unifiedData) {

        log.info("Handling blackDuck scanner parameters: {}", unifiedData);
        log.debug("go to firstRequestRepos method: {}", unifiedData);
        getBlackDuckComponents(unifiedData);
    }

    @SneakyThrows
    public void getBlackDuckComponents(UnifiedDataScanner unifiedData) {
        log.info("firstRequestRepos  parameters: {}", unifiedData);
        String urlToComponent = null;
        HttpClient blackDuckClientRepos = HttpClient.newHttpClient();
        HttpRequest buildRequestRepos = HttpRequest.newBuilder()
                // to be implemented without mock server change the url in URI create in the application.properties file
                .uri(URI.create(blackDuckUrlListOfRepos)).build();
        try {
            HttpResponse<String> response = blackDuckClientRepos.send(buildRequestRepos, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            jsonObject = new JsonObject(responseBody);
            try {
                BlackDuckModel blackDuckModel = objectMapper.readValue(jsonObject.getJson(), BlackDuckModel.class);
                urlToComponent = blackDuckModel.getItems().get_meta().getLinks().stream().filter(c -> c.getRel().equals("components")).findFirst().get().getHref();
                log.info("response from request component and the url to component: {},{}", responseBody, urlToComponent);
            } catch (Exception e) {
                log.error("error occurred at parsing data- the model not match to component response- the object mapper can't convert to black duck model by service name" + e.getMessage());
                throw new ScannerParsingException("error occurred at parsing data- the model not match to component response- the object mapper can't convert to black duck model by service name", e);
            }
        } catch (IOException | InterruptedException e) {
            log.error("IOException|InterruptedException: {},{} return a null response from request component", e.getMessage(), e);
            throw new RuntimeException(e + "return a null response from request component");
        }
        log.debug("go to secondRequestComponent method: {}", urlToComponent);
        getComponentsVulnerabilities(urlToComponent, unifiedData);
    }

    @SneakyThrows
    public void getComponentsVulnerabilities(String urlToComponent, UnifiedDataScanner unifiedData) {
        ArrayList<ScannerTest> tests = new ArrayList<ScannerTest>();
        CountSeverity countSeverity = new CountSeverity();
        log.info("secondRequestComponent parameters: {}", urlToComponent);
        String urlToVulnerabilities = null;
        HttpClient blackDuckClientComponent = HttpClient.newHttpClient();
        HttpRequest buildRequestComponent = HttpRequest.newBuilder()
                // to be implemented without mock server change the url in URI create to urlToComponent
                .uri(URI.create("https://d268581e-1578-4580-8573-0cbd11e65ef2.mock.pstmn.io")).build();
        try {
            HttpResponse<String> response = blackDuckClientComponent.send(buildRequestComponent, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            jsonObject = new JsonObject(responseBody);
            try {
                ComponentModel componentModel = objectMapper.readValue(jsonObject.getJson(), ComponentModel.class);
                ArrayList<Item> ComponentItems = componentModel.getItems();
                for (Item item : ComponentItems) {
                    urlToVulnerabilities = item.get_meta().getLinks().stream().filter(c -> c.getRel().equals("vulnerabilities")).findFirst().get().getHref();
                    log.info("response from request vulnerabilities and the url to vulnerabilities: {},{}", responseBody, urlToVulnerabilities);
                    log.debug("go to thirdRequestVulnerabilities method at index: {}", item);
                    getVulnerabilitiesDetails(urlToVulnerabilities, unifiedData, countSeverity, tests);
                }
            } catch (Exception e) {
                log.error("error occurred at parsing data- the model not match to list of vulnerabilities response- the object mapper can't convert to component model" + e.getMessage());
                throw new ScannerParsingException("error occurred at parsing data- the model not match to list of vulnerabilities response- the object mapper can't convert to component model", e);
            }
        } catch (IOException | InterruptedException e) {
            log.error("IOException|InterruptedException: {},{} return a null response from request vulnerabilities", e.getMessage(), e);
            throw new RuntimeException(e + "return a null response from request vulnerabilities");
        }
    }

    @SneakyThrows
    public void getVulnerabilitiesDetails(String urlVulnerabilities, UnifiedDataScanner unifiedData, CountSeverity countSeverity, ArrayList<ScannerTest> tests) {
        HttpClient blackDuckClientVulnerabilities = HttpClient.newHttpClient();
        HttpRequest buildRequestVulnerabilities = HttpRequest.newBuilder()
                // to be implemented without mock server change the url in URI create to urlVulnerabilities
                .uri(URI.create("https://a25856b2-16e0-4da9-96ec-7aab102bf725.mock.pstmn.io/")).build();
        try {
            HttpResponse<String> response = blackDuckClientVulnerabilities.send(buildRequestVulnerabilities, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            jsonObject = new JsonObject(responseBody);
            try {
                VulnerabilityModel vulnerabilityModel = objectMapper.readValue(jsonObject.getJson(), VulnerabilityModel.class);
                List<TestModel> testList = vulnerabilityModel.getItems();
                log.info("thirdRequestVulnerabilities parameters: {} response from request is: {}", urlVulnerabilities, vulnerabilityModel);
                countSeverity.setCountTests(countSeverity.getCountTests() + (Integer) vulnerabilityModel.getTotalCount());
                log.debug("go to aggregateBlackDuckToUnifiedData method  parameters: {},{}", unifiedData, testList);
                aggregateBlackDuckToUnifiedData(unifiedData, testList, countSeverity, tests);
            } catch (Exception e) {
                log.error("error occurred at parsing data- the model not match to list of tests response- the object mapper can't convert to vulnerability model" + e.getMessage());
                throw new ScannerParsingException("error occurred at parsing data- the model not match to list of tests response- the object mapper can't convert to vulnerability model", e);
            }
        } catch (IOException | InterruptedException e) {
            log.error("IOException|InterruptedException: {},{} return a null response from request thirdRequestVulnerabilities parameters", e.getMessage(), e);
            throw new RuntimeException(e + "return a null response from request thirdRequestVulnerabilities parameters");
        }
    }

    public Severity getSeverity(String severity) {
        Severity severityReturn = switch (severity) {
            case "LOW" -> Severity.LOW;
            case "MEDIUM" -> Severity.MEDIUM;
            default -> Severity.HIGH;
        };
        log.info("fill the severity filed {}", severityReturn);
        return severityReturn;
    }

    public Map<String, String> fillCustomizedFields(TestModel test) {
        Map<String, String> customizedFields = new HashMap<String, String>();
        customizedFields.put(BlackDuckConstants.VULNERABILITY_PUBLISHED_DATE, test.getVulnerabilityPublishedDate().toString());
        customizedFields.put(BlackDuckConstants.VULNERABILITY_UPDATE_DATE, test.getVulnerabilityUpdatedDate().toString());
        customizedFields.put(BlackDuckConstants.AUTHENTICATION, test.getAuthentication());
        customizedFields.put(BlackDuckConstants.ACCESS_VECTOR, test.getAccessVector());
        customizedFields.put(BlackDuckConstants.AVAILABILITY_IMPACT,test.getAvailabilityImpact());
        customizedFields.put(BlackDuckConstants.CWE_ID,test.getCweId());
        customizedFields.put(BlackDuckConstants.ACCESS_COMPLEXITY,test.getAccessComplexity());
        customizedFields.put(BlackDuckConstants.BASE_SCORE,test.getBaseScore());
        customizedFields.put(BlackDuckConstants.IMPACT_SUB_SCORE,test.getImpactSubscore());
        customizedFields.put(BlackDuckConstants.EXPLOITABILITY_SUBS_CORE,test.getExploitabilitySubscore());
        customizedFields.put(BlackDuckConstants.SOURCE,test.getSource());
        customizedFields.put(BlackDuckConstants.INTEGRITY_IMPACT,test.getIntegrityImpact());
        customizedFields.put(BlackDuckConstants.CONFIDENTIALITY_IMPACT,test.getConfidentialityImpact());
        return  customizedFields;
    }


    @SneakyThrows
    public List<ScannerTest> fillDataFromTests(List<TestModel> testList, ArrayList<ScannerTest> tests, CountSeverity countSeverity) {
        try {
            for (TestModel test : testList) {
                fillCountSeverity(test.getSeverity(), countSeverity);
                String id = test.getVulnerabilityName();
                String description = test.getDescription();
                Severity severity = getSeverity(test.getSeverity());
                Map<String, String> customizedFields= fillCustomizedFields(test);
                tests.add(new ScannerTest(customizedFields, id, description, severity, Status.FAILED));
                log.info("fill the data from tests : {}", tests);
            }
        } catch (Exception e) {
            log.error(e.getMessage() + "scanner aggregation failed");
            throw new ScannerAggregationException("scanner aggregation failed" + e.getMessage(), e);
        }
        return tests;
    }

    @SneakyThrows
    public void aggregateBlackDuckToUnifiedData(UnifiedDataScanner unifiedData, List<TestModel> testList, CountSeverity countSeverity, ArrayList<ScannerTest> tests) {
        log.info("Aggregating data for unifiedDataScanner");
        try {
            unifiedData.setNumberOfScans(countSeverity.getCountTests());
            List<ScannerTest> listOfTestsAfterAggregation = fillDataFromTests(testList, tests, countSeverity);
            unifiedData.setListOfTests(listOfTestsAfterAggregation);
            unifiedData.setExecutionDate(LocalDateTime.now());
            unifiedData.setNumberOfRisksLow(countSeverity.getCountLow());
            unifiedData.setNumberOfRisksMedium(countSeverity.getCountMedium());
            unifiedData.setNumberOfRisksHigh(countSeverity.getCountHigh());
            unifiedData.setNumberOfSuccessfulScans(0);
            unifiedData.setScannerType(this.getScannerGroupType());
            log.info("Aggregation to unifiedData complete {}", unifiedData);
        } catch (Exception e) {
            log.error(e.getMessage() + "scanner aggregation failed");
            throw new ScannerAggregationException("scanner aggregation failed" + e.getMessage(), e);
        }
    }

    @SneakyThrows
    public void fillCountSeverity(String severity, CountSeverity countSeverity) {
        log.info("FillCountSeverity");
        try {
            switch (severity) {
                case "LOW" -> countSeverity.setCountLow(countSeverity.getCountLow() + 1);
                case "HIGH" -> countSeverity.setCountHigh(countSeverity.getCountHigh() + 1);
                case "MEDIUM" -> countSeverity.setCountMedium(countSeverity.getCountMedium() + 1);
            }
        } catch (Exception e) {
            log.error(e.getMessage() + "scanner aggregation failed");
            throw new ScannerAggregationException("scanner aggregation failed" + e.getMessage(), e);
        }
    }

    @Override
    public ScannerType getScannerType() {
        return ScannerType.BLACKDUCK;
    }

    @Override
    public ScannerGroupType getScannerGroupType() {
        return ScannerGroupType.SECURITY;
    }
}
