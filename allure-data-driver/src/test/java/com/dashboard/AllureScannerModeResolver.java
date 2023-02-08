package com.dashboard;

import com.dashboard.allure.model.*;
import com.dashboard.model.ScannerTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.ArrayList;
import java.util.List;

public class AllureScannerModeResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == AllureFile.class;
    }
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        AllureFile allureFile = new AllureFile();
        List<ScannerTest> listOfTestsAfterAggrigation = new ArrayList<>();
        List<TestDetails> testsLists = new ArrayList<>();
        testsLists.add(new TestDetails("test1", "1111", "0000", "failed", new Time("10:00", "11:00", 1), true, true, new ArrayList<String>()));
        testsLists.add(new TestDetails("test2", "2222", "0000", "passed", new Time("10:00", "11:00", 1), true, true, new ArrayList<String>()));
        testsLists.add(new TestDetails("test3", "3333", "0000", "failed", new Time("10:00", "11:00", 1), true, true, new ArrayList<String>()));
        List<TestClass> testClasses = new ArrayList<>();
        testClasses.add(new TestClass("testClass", testsLists, "0000"));
        List<TestService> testServices = new ArrayList<>();
        testServices.add(new TestService("TestService", testClasses, "0000"));
        List<AllureGlobalDetails> allureGlobalDetails = new ArrayList<>();
        allureGlobalDetails.add(new AllureGlobalDetails("child", testServices, "0000"));
        allureFile.setChildren(allureGlobalDetails);
        return allureFile;
    }
}
