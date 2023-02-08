package com.dashboard;
import com.dashboard.allure.model.AllureFile;
import com.dashboard.allure.service.AllureDataDriverHandlerService;
import com.dashboard.model.UnifiedDataScanner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, UnifiedDataResolver.class, AllureScannerModeResolver.class})
@Slf4j
public class AllureTest {

    @InjectMocks
    AllureDataDriverHandlerService service;

    @Test
    @DisplayName("AggrigationToUnifiedDataAllureScanner")
    public void AggrigationOnAllureScanner_whenAggrigationFailed_withUnValidScanner(UnifiedDataScanner unifiedDataScanner, AllureFile allureFile){
       service.AggregationToUnifiedDataAllureScanner(allureFile, unifiedDataScanner);
       Assertions.assertEquals(3, unifiedDataScanner.getNumberOfScans());
       Assertions.assertEquals(1, unifiedDataScanner.getNumberOfSuccessfulScans());
       Assertions.assertEquals(2, unifiedDataScanner.getNumberOfRisksHigh());
    }

}
