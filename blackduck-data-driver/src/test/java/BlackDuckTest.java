import com.dashboard.blackduck.model.CountSeverity;
import com.dashboard.blackduck.model.VulnerabilityModel;
import com.dashboard.blackduck.service.BlackDuckDriverHandlerService;
import com.dashboard.model.UnifiedDataScanner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, UnifiedDataResolver.class, VulnerabilityModelResolver.class,CountSeverityResolver.class,ScannerTestListResolver.class})
@Slf4j
public class BlackDuckTest {
    @InjectMocks
    BlackDuckDriverHandlerService serviceBlackDuck;

    @Test
    @DisplayName("AggrigationToUnifiedDataBlackDuckScanner")
    public void AggrigationOnBlackDuckScanner_whenAggrigationFailed_withUnValidScanner(UnifiedDataScanner unifiedDataScanner, VulnerabilityModel vulnerabilityModel, CountSeverity countSeverity, ScannerTests scannerTests){
        serviceBlackDuck.aggregateBlackDuckToUnifiedData(unifiedDataScanner,vulnerabilityModel.getItems(),countSeverity,scannerTests.getTestsList());
        Assertions.assertEquals(5, unifiedDataScanner.getListOfTests().size());
        Assertions.assertEquals(2, unifiedDataScanner.getNumberOfRisksLow());
        Assertions.assertEquals(0, unifiedDataScanner.getNumberOfSuccessfulScans());
    }
}
