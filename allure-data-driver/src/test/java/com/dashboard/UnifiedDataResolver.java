package com.dashboard;

import com.dashboard.model.UnifiedDataScanner;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UnifiedDataResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == UnifiedDataScanner.class;
    }
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        UnifiedDataScanner unifiedDataScanner = new UnifiedDataScanner();
        unifiedDataScanner.setProductName("testAllureUnifiedData");
        unifiedDataScanner.setMicroserviceName("testAllureUnifiedData");
        unifiedDataScanner.setTestName("UnitTestOftestAllureUnifiedData");
        unifiedDataScanner.setVersion("0.0.0");
        unifiedDataScanner.setScannerName("allure");
        return unifiedDataScanner;
    }

}
