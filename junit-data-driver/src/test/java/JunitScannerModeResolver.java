import com.dashboard.junit.model.Failure;
import com.dashboard.junit.model.TestCase;
import com.dashboard.junit.model.Testsuite;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.ArrayList;
import java.util.List;

public class JunitScannerModeResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Testsuite.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Testsuite junitFile = new Testsuite();
        junitFile.setName("JunitName");
        List<TestCase> testsList = new ArrayList<>();
        testsList.add(new TestCase("test1", "1111","10.10", null, null));
        testsList.add(new TestCase("test2", "2222","11.30",new Failure("a","111"), "system-out1"));
        testsList.add(new TestCase("test3", "3333","14.55",new Failure("a","222"), "system-out1"));
        testsList.add(new TestCase("test4", "1111","10.10",new Failure("a","333"), "system-out1"));

        junitFile.setTestcase(testsList);
        return junitFile;
    }
}
