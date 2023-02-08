package com.dashbord.IntregrationTests;

import com.dashboard.ScannerDataManagerApplication;
import com.dashboard.model.UnifiedDataScanner;
import com.dashboard.repository.UnifiedDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ScannerDataManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UploadScannerFileTest extends IntegrationTesting {
    @Autowired
    UnifiedDataRepository unifiedDataRepository;
    UUID uuid;
    HttpHeaders headers = null;
    MultiValueMap<String, Object> body = null;
    UnifiedDataScanner unifiedData = null;
    File file = null;

    @Before
    public void setup() throws Exception {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap<>();
        body.add("microserviceName", "microserviceName");
        body.add("branchVersion", "branchVersion");
        body.add("testName", "testName");
    }

    @After
    public void cleanUpEach() throws IOException {
        if (unifiedData != null)
            unifiedDataRepository.delete(unifiedData);
        Files.delete(file.toPath());
    }


    @Test
    public void AllureUploadScannerFileTest() {
        uuid = UUID.randomUUID();
        body.add("buildNumber", String.valueOf(uuid));
        setFile(body, "allure.json");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(createURLWithPort("/scanners/upload-file/APEX/Allure"),
                requestEntity, Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        unifiedData = unifiedDataRepository.findByBuildNumber(String.valueOf(uuid)).orElseThrow();
        assertThat(unifiedData.getNumberOfScans(), equalTo(8));
        assertThat(unifiedData.getNumberOfRisksHigh(), equalTo(0));
        assertThat(unifiedData.getNumberOfSuccessfulScans(), equalTo(8));
    }

    @Test
    public void JunitUploadScannerFileTest() {
        uuid = UUID.randomUUID();
        body.add("buildNumber", String.valueOf(uuid));
        setFile(body, "junit.xml");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(createURLWithPort("/scanners/upload-file/APEX/Junit"),
                requestEntity, Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        unifiedData = unifiedDataRepository.findByBuildNumber(String.valueOf(uuid)).orElseThrow();
        assertThat(unifiedData.getNumberOfScans(), equalTo(12));
        assertThat(unifiedData.getNumberOfRisksHigh(), equalTo(1));
        assertThat(unifiedData.getNumberOfSuccessfulScans(), equalTo(11));

    }


    @Test
    public void PrismaUploadScannerFileTest() {
        uuid = UUID.randomUUID();
        body.add("buildNumber", String.valueOf(uuid));
        setFile(body, "prisma.json");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(createURLWithPort("/scanners/upload-file/APEX/Prisma"),
                requestEntity, Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        unifiedData = unifiedDataRepository.findByBuildNumber(String.valueOf(uuid)).orElseThrow();
        assertThat(unifiedData.getNumberOfRisksHigh(), equalTo(141));
//        assertThat(unifiedData.getNumberOfSuccessfulScans(),equalTo(0));
        assertThat(unifiedData.getNumberOfScans(), equalTo(141));

    }

    @Test
    public void OpenApiUploadScannerFileTest() {
        uuid = UUID.randomUUID();
        body.add("buildNumber", String.valueOf(uuid));
        setFile(body, "openApiScanResult.json");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(createURLWithPort("/scanners/upload-file/APEX/OpenApi"),
                requestEntity, Void.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        unifiedData = unifiedDataRepository.findByBuildNumber(String.valueOf(uuid)).orElseThrow();
        assertThat(unifiedData.getNumberOfScans(), equalTo(0));
        assertThat(unifiedData.getNumberOfRisksHigh(), equalTo(0));
        assertThat(unifiedData.getNumberOfSuccessfulScans(), equalTo(0));
    }

    private void setFile(MultiValueMap<String, Object> body, String fileName) {
        try {
            file = new File(fileName);
            Files.copy(ResourceUtils.getFile("classpath:" + fileName).toPath(), file.toPath());
        } catch (Exception e) {
            log.info("file already exists so no need copy again");
        }
        body.add("scannerDataFile", new org.springframework.core.io.ClassPathResource(fileName));
    }
}
