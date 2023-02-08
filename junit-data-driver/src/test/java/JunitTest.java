import com.dashboard.junit.model.Testsuite;
import com.dashboard.junit.service.JunitDataDriverHandlerService;
import com.dashboard.model.UnifiedDataScanner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, UnifiedDataResolver.class, JunitScannerModeResolver.class})
@Slf4j

public class JunitTest {
    @InjectMocks
    JunitDataDriverHandlerService ServiceJunit;

    @Test
    @DisplayName("AggrigationToUnifiedDataJunitScanner")
    public void AggrigationOnJunitScanner_whenAggrigationFailed_withUnValidScanner(UnifiedDataScanner unifiedDataScanner, Testsuite junitFile){
        ServiceJunit.aggregateJunitToUnifedData(junitFile, unifiedDataScanner);
        Assertions.assertEquals(4, unifiedDataScanner.getNumberOfScans());
        Assertions.assertEquals(3,unifiedDataScanner.getNumberOfScans()-unifiedDataScanner.getNumberOfSuccessfulScans());
        Assertions.assertEquals(1, unifiedDataScanner.getNumberOfSuccessfulScans());
    }
}
