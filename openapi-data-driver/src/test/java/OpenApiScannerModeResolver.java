import com.dashboard.openapi.model.ChangedOperation;
import com.dashboard.openapi.model.OpenApiScannerFile;
import com.dashboard.openapi.model.Operation;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.ArrayList;
import java.util.List;

public class OpenApiScannerModeResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == OpenApiScannerFile.class;
    }
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        OpenApiScannerFile openApiScannerFile = new OpenApiScannerFile();
        Operation operation1 = new Operation(null,"OpenApiTest1", "111");
        Operation operation2 = new Operation(null,"OpenApiTest2", "222");
        ChangedOperation changedOperation1 = new ChangedOperation();
        changedOperation1.setOldOperation(operation1);
        changedOperation1.setNewOperation(operation2);
        ChangedOperation changedOperation2 = new ChangedOperation();
        changedOperation2.setOldOperation(operation1);
        changedOperation2.setNewOperation(operation2);
        List<ChangedOperation> changedOperationList = new ArrayList<>();
        changedOperationList.add(changedOperation1);
        changedOperationList.add(changedOperation2);
        openApiScannerFile.setChangedElements(changedOperationList);
        return openApiScannerFile;
    }
}
