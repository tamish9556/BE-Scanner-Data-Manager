package com.dashboard.service;

import com.dashboard.model.IdentifierScanner;
import com.dashboard.model.UnifiedDataScanner;
import com.dashboard.repository.UnifiedDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UnifiedDataService {
    @Autowired
    private UnifiedDataRepository repository;
    @Autowired
    MongoTemplate mongoTemplate;


    public UnifiedDataScanner addUnifiedData(UnifiedDataScanner completeUnifiedData) {
        repository.save(completeUnifiedData);
        return completeUnifiedData;
    }

    public UnifiedDataScanner showMoreDetailsById(String id) {
        return repository.findById(id).orElseThrow();
    }

    public List<String> getProductsNames(String scannerName){
        Query query = new Query();
        query.fields().include("scannerName");
        query.fields().include("productName");
        if (scannerName != null && !scannerName.isEmpty()){
            Criteria.where("scannerName").is(scannerName);
        }
        return mongoTemplate.findDistinct(query,"productName", UnifiedDataScanner.class, String.class);
    }

    public List<String> getMicroservicesNames(String productName, String scannerName) {
        List<Criteria> criteria = new ArrayList<>();
        Query query = new Query();
        query.fields().include("scannerName");
        query.fields().include("productName");
        query.fields().include("microserviceName");
        if (scannerName != null && !scannerName.isEmpty()){
            criteria.add(Criteria.where("scannerName").is(scannerName));
        }
        if (productName != null && !productName.isEmpty()){
            criteria.add(Criteria.where("productName").is(productName));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
            log.debug("Logback from getMicroservicesNames:{},{}", query,criteria);
        }
        else {
            log.warn("There is no criteria in the list of criteria");
        }
        List<UnifiedDataScanner> scannersByCriteria = mongoTemplate.find(query, UnifiedDataScanner.class);
        log.debug("Logback from getMicroservicesNames  all the scanners by the query:{}", scannersByCriteria);
        return mongoTemplate.findDistinct(query,"microserviceName", UnifiedDataScanner.class, String.class);

    }

    public List<String> getVersionsByProductNameAndMicroserviceName(String productName, String microserviceName, String scannerName) {
        List<Criteria> criteria = new ArrayList<>();
        Query query = new Query();
        query.fields().include("scannerName");
        query.fields().include("productName");
        query.fields().include("microserviceName");
        query.fields().include("versions");
        if (scannerName != null && !scannerName.isEmpty()){
            criteria.add(Criteria.where("scannerName").is(scannerName));
        }
        if (productName != null && !productName.isEmpty()){
            criteria.add(Criteria.where("productName").is(productName));
        }
        if (microserviceName != null && !microserviceName.isEmpty()){
            criteria.add(Criteria.where("microserviceName").is(microserviceName));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
            log.debug("Logback from getVersionsByProductNameAndMicroserviceName:{},{}", query,criteria);
        }
        else {
            log.warn("There is no criteria in the list of criteria");
        }
        log.debug("Logback from getVersionsByProductNameAndMicroserviceName  all the scanners by the query");
        return mongoTemplate.findDistinct(query,"version", UnifiedDataScanner.class, String.class);
    }

    public List<UnifiedDataScanner> getReportsByProductNameAndMicroserviceNameAndVersion(String productName, String microserviceName, String version, String scannerName) {
        List<Criteria> criteria = new ArrayList<>();
        Query query = new Query();
        query.fields().include("id");
        query.fields().include("scannerName");
        query.fields().include("productName");
        query.fields().include("microserviceName");
        query.fields().include("version");
        query.fields().include("buildNumber");
        if (scannerName != null && !scannerName.isEmpty()){
            criteria.add(Criteria.where("scannerName").is(scannerName));
        }
        if (productName != null && !productName.isEmpty()){
            criteria.add(Criteria.where("productName").is(productName));
        }
        if (microserviceName != null && !microserviceName.isEmpty()){
            criteria.add(Criteria.where("microserviceName").is(microserviceName));
        }
        if (version != null && !version.isEmpty()){
            criteria.add(Criteria.where("version").is(version));
        }
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
            log.debug("Logback from getReportsByProductNameAndMicroserviceNameAndVersion:{},{}", query,criteria);
        }
        else {
            log.warn("There is no criteria in the list of criteria");
        }
        log.debug("Logback from getReportsByProductNameAndMicroserviceNameAndVersion  all the scanners by the query");
        return mongoTemplate.find(query, UnifiedDataScanner.class);
    }

    public List<IdentifierScanner> getIdentifierScannerByProductNameAndMicroserviceName(String productName, String microserviceName, String version, String scannerName) {
        List<UnifiedDataScanner> listReports = getReportsByProductNameAndMicroserviceNameAndVersion(productName,microserviceName, version, scannerName);
        List<IdentifierScanner> identifierScannerList = new ArrayList<>();
        IdentifierScanner identifierScanner;
        for(UnifiedDataScanner unifiedData : listReports){
            identifierScanner = new IdentifierScanner(unifiedData.getId(),unifiedData.getBuildNumber());
            identifierScannerList.add(identifierScanner);
        }
        return identifierScannerList;
    }
}

