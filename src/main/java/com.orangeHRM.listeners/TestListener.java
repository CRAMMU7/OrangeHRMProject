package com.orangeHRM.listeners;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;

public class TestListener implements ITestListener , IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, java.lang.reflect.Method testMethod) {
        annotation.setRetryAnalyzer(com.orangeHRM.utilities.RetryAnalyzer.class);
    }
    //Triggered when suite starts
    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
        ExtentManager.getReporter();
    }

    //Triggered when test starts
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.startTest(testName);
        ExtentManager.logStep("Test  started " + testName);

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        if (!result.getTestClass().getName().toLowerCase().contains("api")) {
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed successfully", "Test End: " + testName + " - ✔ Test Passed");
        } else {
            ExtentManager.logStepValidationForApi("Test End: " + testName + " - ✔ Test Passed");

        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String failureMessage = result.getThrowable().getMessage();
        ExtentManager.logStep(failureMessage);
        if(!result.getTestClass().getName().toLowerCase().contains("api")) {
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Failed!", "Test End: " + testName + " - ❌ Test Failed");
        }
        else{
            ExtentManager.logStepValidationForApi("Test End: " + testName + " - ❌ Test Failed");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test Skipped " + testName);
    }

    //Triggered when suite ends
    @Override
    public void onFinish(ITestContext context) {
        //Flush extent report
        ExtentManager.endTest();
    }
}
