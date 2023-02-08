import com.dashboard.model.UnifiedDataScanner;
import com.dashboard.prisma.model.PrismaFile;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.dashboard.prisma.service.PrismaDataDriverHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, UnifiedDataResolver.class, PrismaScannerModeResolver.class})
@Slf4j
public class PrismaTest {
    @InjectMocks
    PrismaDataDriverHandlerService service;

    @Test
    @DisplayName("AggregationToUnifiedDataPrismaScanner")
    @SneakyThrows
    public void aggregationPrismaWhenAggregationFailed(UnifiedDataScanner unifiedDataScanner, PrismaFile prismaFile){
        service.aggregationToUnifiedDataPrismaScanner(prismaFile, unifiedDataScanner);
        Assertions.assertEquals(141, unifiedDataScanner.getNumberOfScans());
        Assertions.assertEquals(0, unifiedDataScanner.getNumberOfSuccessfulScans());
        Assertions.assertEquals(141, unifiedDataScanner.getNumberOfRisksHigh());
    }
}
