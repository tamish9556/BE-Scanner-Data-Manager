package com.dashboard.controller;

import com.dashboard.controller.ifc.ScannersApi;
import com.dashboard.model.IdentifierScanner;
import com.dashboard.model.ScannerModel;
import com.dashboard.model.UnifiedDataScanner;
import com.dashboard.service.ScannerDataService;
import com.dashboard.service.UnifiedDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/scanners")
@Slf4j

public class ScannerDataController implements ScannersApi {
    @Autowired
    private ScannerDataService scannerDataService;
    @Autowired
    private UnifiedDataService unifiedDataService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UnifiedDataScanner> showMoreDetailsById(@PathVariable String id) {
        try {
            UnifiedDataScanner scanner = unifiedDataService.showMoreDetailsById(id);
            log.info("Logback from get REST api for showMoreDetailsById  parameters are: {} and the scanner is {}", scanner, id);
            return ResponseEntity.ok(scanner);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    @Override
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/upload-file/{productName}/{scannerName}",
            produces = { "*/*", "application/json" },
            consumes = { "multipart/form-data" }
    )

    public ResponseEntity<Void> uploadScannerFile(
            @PathVariable String productName,
            @PathVariable String scannerName,
            @RequestParam(value = "microserviceName") String microserviceName,
            @RequestParam(value = "branchVersion") String branchVersion,
            @RequestParam(value = "buildNumber") String buildNumber,
            @RequestPart( required = false,value = "scannerDataFile") MultipartFile scannerDataFile,
            @RequestParam(value = "testName", required = false) String testName) {
        try {
            log.info("Upload new scanner row. Details: {}, {}, {}", productName, microserviceName, buildNumber);
            scannerDataService.uploadNewScannerFile(scannerDataFile, productName, microserviceName, branchVersion, scannerName, testName, buildNumber);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Internal server exception {}", e.getMessage());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Override
    @GetMapping("/graphs-data")
    public ResponseEntity<Map<String, List<ScannerModel>>> getScannerDataByFilter(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String scannerType,
            @RequestParam(required = false) String productName ,
            @RequestParam(required = false)String serviceName,
            @RequestParam(required = false) String branchName) {
        log.info("go to {} method that get {},{},{},{},{},{}" ,scannerDataService,productName,serviceName,  branchName,fromDate, toDate, scannerType);
        HashMap scannersData = scannerDataService.getAllScannersData(productName, serviceName,  branchName,fromDate, toDate, scannerType);
        log.info("return the list that contain all data scanner {}", scannersData);
        return ResponseEntity.ok().body(scannersData);
    }

    @Override
    @GetMapping("/products-names")
    public ResponseEntity<List<String>> getAllProductsNames(@RequestParam(required = false) String scannerName) {
        try {
            List<String> listOfDistinctProducts = unifiedDataService.getProductsNames(scannerName);
            log.info("Logback from get REST Api for getAllProductsNames parameters are: {} and the scannerName is {}", listOfDistinctProducts, scannerName);
            return ResponseEntity.ok().body(listOfDistinctProducts);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Override
    @GetMapping("/microservices-names")
    public ResponseEntity<List<String>> getMicroservicesNames(@RequestParam String productName, @RequestParam(required = false) String scannerName) {
        try {
            List<String> listOfDistinctMicroservicesNames = unifiedDataService.getMicroservicesNames(productName, scannerName);
            log.info("Logback from get REST Api for getMicroservicesNames parameters are: {} and the productName, scannerName is {} {}", listOfDistinctMicroservicesNames, scannerName);
            return ResponseEntity.ok().body(listOfDistinctMicroservicesNames);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    @GetMapping("/versions")
    public ResponseEntity<List<String>> getVersionsByProductNameAndMicroserviceName(@RequestParam String productName, @RequestParam String microserviceName, @RequestParam(required = false) String scannerName) {
        try {
            List<String> listOfDistinctVersions = unifiedDataService.getVersionsByProductNameAndMicroserviceName(productName, microserviceName, scannerName);
            log.info("Logback from get REST Api for getVersionsByProductNameAndMicroserviceName parameters are: {} and the productName, microserviceName, scannerName is {} {} {}", listOfDistinctVersions, productName, microserviceName, scannerName);
            return ResponseEntity.ok().body(listOfDistinctVersions);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    @GetMapping("/reports")
    public ResponseEntity<List<IdentifierScanner>> getIdentifierScanner(@RequestParam String productName, @RequestParam String microserviceName, @RequestParam String version, @RequestParam(required = false) String scannerName) {
        try {
            List<IdentifierScanner> listOfDistinctIdentifierScanner = unifiedDataService.getIdentifierScannerByProductNameAndMicroserviceName(productName, microserviceName, version, scannerName);
            log.info("Logback from get REST Api for getIdentifierScanner parameters are: {} and the productName, microserviceName, version, scannerName is {} {} {}", listOfDistinctIdentifierScanner, productName, microserviceName, version, scannerName);
            return ResponseEntity.ok().body(listOfDistinctIdentifierScanner);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    @GetMapping("/scanners-names")
    public ResponseEntity<List<String>> getScannersNames() {
        try {
            List<String> listScannersNames = scannerDataService.getListScannerName();
            log.info("Logback from get REST Api for getScannersNames parameters are: {}", listScannersNames);
            return ResponseEntity.ok().body(listScannersNames);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
