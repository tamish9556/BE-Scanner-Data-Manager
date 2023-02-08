package com.dashboard.prisma.service;
import com.dashboard.Exceptions.ScannerParsingException;
import com.dashboard.model.*;
import com.dashboard.prisma.PrismaConstant;
import com.dashboard.prisma.model.EntityInfo;
import com.dashboard.prisma.model.PrismaFile;
import com.dashboard.prisma.model.Report;
import com.dashboard.service.ifc.DataDriverHandlerServiceIfc;
import com.dashboard.model.ScannerType;
import com.dashboard.Exceptions.ScannerAggregationException;
import com.dashboard.model.*;
import com.dashboard.prisma.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PrismaDataDriverHandlerService implements DataDriverHandlerServiceIfc {
    ObjectMapper objectMapper = new ObjectMapper();
    @SneakyThrows
    @Override
    public void handleAndParsingData(MultipartFile file, UnifiedDataScanner unifiedData) {
        try {
            PrismaFile prismaFile = objectMapper.readValue(file.getInputStream(), PrismaFile.class);
            unifiedData = aggregationToUnifiedDataPrismaScanner(prismaFile, unifiedData);
            unifiedData.setScannerType(this.getScannerGroupType());
            log.debug("UnifiedData from Prisma scanner" + unifiedData);
        } catch (Exception e) {
            log.error("Error on handleAndParsingData" + e.getMessage());
            throw new ScannerParsingException("Error in parsing prisma file to unifiedData",e);
        }
    }

    @SneakyThrows
    private List<ScannerTest> aggregateTestsList(List<Report> vulnerabilities){
        Map<String, String> customizedData = new HashMap<>();
        List<ScannerTest> scannerTests = new ArrayList<>();
        try {
            vulnerabilities.forEach(report -> {
                ScannerTest oneScannerTest = new ScannerTest();
                oneScannerTest.setId(report.getId().toString());
                oneScannerTest.setDescription(report.getDescription());
                oneScannerTest.setStatus(Status.FAILED);
                customizedData.put(PrismaConstant.TEXT,report.getText());
                customizedData.put(PrismaConstant.CVSS, report.getCvss().toString());
                customizedData.put(PrismaConstant.CVE,report.getCve());
                customizedData.put(PrismaConstant.CAUSE, report.getCause());
                customizedData.put(PrismaConstant.VEC_STR,report.getVecStr());
                customizedData.put(PrismaConstant.TITLE, report.getTitle());
                customizedData.put(PrismaConstant.EXPLOIT, report.getExploit());
                customizedData.put(PrismaConstant.LINK,report.getLink());
                customizedData.put(PrismaConstant.TYPE, report.getType());
                customizedData.put(PrismaConstant.PACKAGE_NAME,report.getPackageName());
                customizedData.put(PrismaConstant.PACKAGE_VERSION,report.getPackageVersion());
                customizedData.put(PrismaConstant.LAYER_TIME,report.getLayerTime().toString());
                customizedData.put(PrismaConstant.DISCOVERED,report.getDiscovered());
                oneScannerTest.setCustomizedFields(customizedData);
                switch (report.getSeverity()) {
                    case "high":
                        oneScannerTest.setSeverity(Severity.HIGH);
                    case "medium":
                        oneScannerTest.setSeverity(Severity.MEDIUM);
                    case "low":
                        oneScannerTest.setSeverity(Severity.LOW);
                        break;
                }
                scannerTests.add(oneScannerTest);
            });
        }
        catch (Exception e){
            throw new ScannerAggregationException("Error in AggregateTestsList", e);
        }
        return scannerTests;
    }

    @SneakyThrows
    private Map<String, String> aggregateCustomizedData(PrismaFile prismaFile){
        EntityInfo entityInfo = prismaFile.getEntityInfo();
        Map<String, String> customizedData = new HashMap<>();
        try {
            customizedData.put(PrismaConstant._ID, prismaFile.get_id());
            customizedData.put(PrismaConstant.TIME, prismaFile.getTime().toString());
            customizedData.put(PrismaConstant.JOB_NAME, prismaFile.getJobName());
            customizedData.put(PrismaConstant.BUILD, prismaFile.getBuild());
            customizedData.put(PrismaConstant.PASS, prismaFile.getPass().toString());
            customizedData.put(PrismaConstant.VERSION, prismaFile.getVersion());
            customizedData.put(PrismaConstant._ID, entityInfo.get_id());
            customizedData.put(PrismaConstant.TYPE, entityInfo.getType());
            customizedData.put(PrismaConstant.HOST_NAME, entityInfo.getHostname());
            customizedData.put(PrismaConstant.SCAN_TIME, entityInfo.getScanTime().toString());
            customizedData.put(PrismaConstant.DISTRO, entityInfo.getDistro());
            customizedData.put(PrismaConstant.PACKAGE_MANAGER, entityInfo.getPackageManager().toString());
            customizedData.put(PrismaConstant.ID, entityInfo.getId());
            customizedData.put(PrismaConstant.CREATION_TIME, entityInfo.getCreationTime().toString());
            customizedData.put(PrismaConstant.VULNERABILITIES_COUNT, entityInfo.getVulnerabilitiesCount().toString());
            customizedData.put(PrismaConstant.COMPLIANCE_ISSUES_COUNT, entityInfo.getComplianceIssuesCount().toString());
            if(entityInfo.getVulnerabilitiesRiskScore() == null){
                customizedData.put(PrismaConstant.VULNERABILITIES_RISK_SCORE, "");
            }
            else{
                customizedData.put(PrismaConstant.VULNERABILITIES_RISK_SCORE, entityInfo.getVulnerabilitiesRiskScore().toString());
            }
            customizedData.put(PrismaConstant.COMPLIANCE_RISK_SCORE, entityInfo.getComplianceRiskScore().toString());
            customizedData.put(PrismaConstant.TOP_LAYER, entityInfo.getTopLayer());
            customizedData.put(PrismaConstant.SCAN_VERSION, entityInfo.getScanVersion());
            customizedData.put(PrismaConstant.FIRST_SCAN_TIME, entityInfo.getFirstScanTime().toString());
            customizedData.put(PrismaConstant.ERR, entityInfo.getErr());
            customizedData.put(PrismaConstant.SCAN_ID, entityInfo.getScanID().toString());
            customizedData.put(PrismaConstant.TRUST_STATUS, entityInfo.getTrustStatus());
            customizedData.put(PrismaConstant.APP_EMBEDDED, entityInfo.getAppEmbedded().toString());
        }
        catch (Exception e){
            throw new ScannerAggregationException("Error in AggregateCustomizedData",e);
        }
        return customizedData;
    }

    @SneakyThrows
    public UnifiedDataScanner aggregationToUnifiedDataPrismaScanner(PrismaFile prismaFile, UnifiedDataScanner unifiedDataScanner) {
        try {
            List<Report> vulnerabilities = prismaFile.getEntityInfo().getVulnerabilities();
            if (vulnerabilities != null) {
                unifiedDataScanner.setListOfTests(aggregateTestsList(vulnerabilities));
                unifiedDataScanner.setNumberOfScans(unifiedDataScanner.getListOfTests().size());
                unifiedDataScanner.setNumberOfSuccessfulScans(0);
                unifiedDataScanner.setNumberOfRisksHigh(unifiedDataScanner.getListOfTests().size());
                unifiedDataScanner.setExecutionDate(prismaFile.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                unifiedDataScanner.setCustomizedFields(aggregateCustomizedData(prismaFile));
                unifiedDataScanner.setScannerType(getScannerGroupType());
                return unifiedDataScanner;
            }
        }
        catch (Exception e){
            log.error("Error occur in AggregationToUnifiedDataPrismaScanner" + e.getMessage());
            throw new ScannerAggregationException("Error in aggregate to unifiedData",e);
        }
        return null;
    }

    @Override
    public ScannerType getScannerType() {
        return ScannerType.PRISMA;
    }

    @Override
    public ScannerGroupType getScannerGroupType() {
        return ScannerGroupType.TEST_REPORT;
    }
}
