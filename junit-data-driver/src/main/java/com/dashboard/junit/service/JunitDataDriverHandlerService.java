package com.dashboard.junit.service;

import com.dashboard.Exceptions.ScannerAggregationException;
import com.dashboard.Exceptions.ScannerParsingException;
import com.dashboard.junit.JunitConstant;
import com.dashboard.junit.model.TestCase;
import com.dashboard.junit.model.Testsuite;
import com.dashboard.model.*;
import com.dashboard.service.ifc.DataDriverHandlerServiceIfc;
import com.dashboard.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JunitDataDriverHandlerService implements DataDriverHandlerServiceIfc {
    @Override
    @SneakyThrows
    public void handleAndParsingData(MultipartFile file, UnifiedDataScanner unifiedData) {

        try {
            log.debug("Junit file is uploading. file name: {}, Build number: {}", file.getName(), unifiedData.getBuildNumber());
            JAXBContext jaxbContext = JAXBContext.newInstance(Testsuite.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Testsuite junit = (Testsuite) unmarshaller.unmarshal(file.getInputStream());
            aggregateJunitToUnifedData(junit, unifiedData);
            unifiedData.setScannerType(this.getScannerGroupType());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception while load Junit file. file name: {}, Build number: {}, e: {}", file.getName(), unifiedData.getBuildNumber(), e.getMessage());
            throw new ScannerParsingException("Exception while load Junit file", e);
        }
    }
    @SneakyThrows
    public void aggregateJunitToUnifedData(Testsuite testsuite, UnifiedDataScanner unifiedData) {
        try {
            int countFailedScanners = 0;
            unifiedData.setExecutionDate(LocalDateTime.now());
            List<ScannerTest> listS = new ArrayList<ScannerTest>();
            for (TestCase test : testsuite.getTestcase()) {
                ScannerTest s = new ScannerTest();
                Map<String, String> data = new HashMap<String, String>();
                data.put(JunitConstant.CLASS_NAME, test.getClassname());
                data.put(JunitConstant.TIME, test.getTime());
                if (test.getFailure() != null) {
                    s.setStatus(Status.FAILED);
                    s.setSeverity(Severity.HIGH);
                    countFailedScanners++;
                    data.put(JunitConstant.TYPE, test.getFailure().getType());
                    data.put(JunitConstant.MESSAGE, test.getFailure().getMessage());
                    data.put(JunitConstant.SYSTEM_OUT, test.getSystemOut());
                } else {
                    s.setStatus(Status.PASSED);
                }
                s.setDescription(test.getName());
                listS.add(s);
                s.setCustomizedFields(data);
            }
            Map<String, String> customizedFields = new HashMap<String, String>();
            customizedFields.put(JunitConstant.TIME, testsuite.getTime());
            customizedFields.put(JunitConstant.NAME, testsuite.getName());
            unifiedData.setCustomizedFields(customizedFields);
            unifiedData.setListOfTests(listS);
            unifiedData.setNumberOfScans(listS.size());
            unifiedData.setNumberOfRisksHigh(countFailedScanners);
            unifiedData.setNumberOfSuccessfulScans(listS.size() - countFailedScanners);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new ScannerAggregationException(e.getMessage(), e);
        }

    }

    @Override
    public ScannerType getScannerType() {
        return ScannerType.JUNIT;
    }

    @Override
    public ScannerGroupType getScannerGroupType() { return ScannerGroupType.TEST_REPORT;}
}
