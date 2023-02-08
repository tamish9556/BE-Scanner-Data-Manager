import com.dashboard.model.UnifiedDataScanner;
import com.dashboard.openapi.model.OpenApiScannerFile;
import com.dashboard.openapi.service.OpenAPIDataDriverHandlerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, UnifiedDataResolver.class, OpenApiScannerModeResolver.class})
@Slf4j
public class OpenApiTest {
    @InjectMocks
    OpenAPIDataDriverHandlerService service;

    @Test
    @DisplayName("AggregationToUnifiedDataOpenApiScanner")
    @SneakyThrows
    public void AggrigationOnOpenApiScanner_whenAggrigationFailed_withUnValidScanner(UnifiedDataScanner unifiedDataScanner, OpenApiScannerFile openApiScannerFile){
        service.AggregationToUnifiedDataOpenApi(openApiScannerFile, unifiedDataScanner);
        Assertions.assertEquals(2, unifiedDataScanner.getNumberOfScans());
        Assertions.assertEquals(0, unifiedDataScanner.getNumberOfSuccessfulScans());
        Assertions.assertEquals(2, unifiedDataScanner.getNumberOfRisksHigh());
    }


}
