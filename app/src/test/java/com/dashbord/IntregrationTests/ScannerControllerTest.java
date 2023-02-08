package com.dashbord.IntregrationTests;

import com.dashboard.ScannerDataManagerApplication;
import com.dashboard.model.IdentifierScanner;
import com.dashboard.model.ScannerModel;
import com.dashboard.model.UnifiedDataScanner;
import com.dashboard.repository.UnifiedDataRepository;
import com.dashboard.service.ScannerDataService;
import com.dashboard.service.UnifiedDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ScannerDataManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class ScannerControllerTest extends IntegrationTesting {
    @Autowired
    UnifiedDataRepository unifiedDataRepository;
    MultiValueMap<String, String> body = null;
    @Autowired
    UnifiedDataService unifiedDataService;
    @Autowired
    ScannerDataService scannerDataService;
    UnifiedDataScanner unifiedData;

    @Before
    public void setup() {
        List<UnifiedDataScanner> all = unifiedDataRepository.findAll();
        if (!all.isEmpty()) {
            unifiedData = all.get(1);
        }
        headers = new HttpHeaders();
        body = new LinkedMultiValueMap<>();
        body.add("fromDate", "2018-01-01");
        body.add("toDate", "2020-01-01");
        body.add("scannerType", "SECURITY");
        body.add("productName", "");
        body.add("serviceName", "");
        body.add("branchName", "");
    }

    @Test
    public void showMoreDetailsByIdTest() {
        if (unifiedData == null) return;
        String Id = unifiedData.getId();
        String url = createURLWithPort("/scanners/" + Id);
        ResponseEntity<UnifiedDataScanner> response = this.restTemplate
                .getForEntity(url, UnifiedDataScanner.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), unifiedData);

    }

    @Test
    public void getScannerDataByFilterTest() {
        LocalDate toDate  = LocalDate.now();
        LocalDate fromDate= LocalDate.of(toDate.getYear()-1,toDate.getMonth(),toDate.getDayOfMonth());
        String url = createURLWithPort("/scanners/graphs-data?fromDate="+ fromDate+"&toDate="+toDate);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, List<ScannerModel>>> response = this.restTemplate
                .exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void getAllProductsNamesTest() {
        String url = createURLWithPort("/scanners/products-names");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<List<String>> response = this.restTemplate
                .exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
                });
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<String> productNames = unifiedDataService.getProductsNames(null);
        assertEquals(response.getBody(), productNames);

    }

    @Test
    public void getMicroservicesNamesTest() {
        if (unifiedData == null) return;
        String productName = unifiedData.getProductName();
        String url = createURLWithPort("/scanners/microservices-names?productName=" + productName);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<List<String>> response = this.restTemplate
                .exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        List<String> microserviceNames = unifiedDataService.getMicroservicesNames(productName, null);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), microserviceNames);

    }

    @Test
    public void getVersionsByProductNameAndMicroserviceNameTest() {
        if (unifiedData == null) return;
        String productName = unifiedData.getProductName();
        String microserviceName = unifiedData.getMicroserviceName();
        String url = createURLWithPort("/scanners/versions?productName=" + productName + "&microserviceName=" + microserviceName);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<List<String>> response = this.restTemplate
                .exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        List<String> versions = unifiedDataService.getVersionsByProductNameAndMicroserviceName(productName, microserviceName, null);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), versions);

    }

    @Test
    public void getIdentifierScannerTest() {
        if (unifiedData == null) return;
        String productName = unifiedData.getProductName();
        String microserviceName = unifiedData.getMicroserviceName();
        String version = unifiedData.getVersion();
        String url = createURLWithPort("/scanners/reports?productName=" + productName + "&microserviceName=" + microserviceName + "&version=" + version);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<List<IdentifierScanner>> response = this.restTemplate
                .exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        List<IdentifierScanner> identifierScannerList = unifiedDataService.getIdentifierScannerByProductNameAndMicroserviceName(productName, microserviceName, version, null);
        assertEquals(response.getBody(), identifierScannerList);
    }

    @Test
    public void getScannersNamesTest() {
        String url = createURLWithPort("/scanners/scanners-names");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<ArrayList<String>> response = this.restTemplate
                .exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
        ArrayList<String> scannerNames = scannerDataService.getListScannerName();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), scannerNames);
    }


}
