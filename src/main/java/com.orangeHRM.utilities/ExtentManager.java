package com.orangeHRM.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {
    private static ExtentReports extent;//Common Info of Report--SystemInfo(OS,Java Version,User name)
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();//Create Status for each Test Methods
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    //Initialize Extent Reports
    public static ExtentReports getReporter() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);//UI of Report--Name ,Title,Theme
            spark.config().setReportName("Automation Test report");
            spark.config().setDocumentTitle("OrangeHRM  report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
        }
        return extent;
    }

    //Start the test
    public static ExtentTest startTest(String testName) {
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }


    //End the test
    public static void endTest() {
        getReporter().flush();
    }

    //get current thread's test
    public static ExtentTest getTest() {
        return test.get();
    }

    //get name of the current  test
    public static String getTestName() {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            return currentTest.getModel().getName();
        } else {
            return "No test is currently active for the thread";
        }

    }

    //Log a step
    public static void logStep(String logMessage) {
        getTest().info(logMessage);
    }

    //Log a step validation with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String logMessage,String screenShotMessage) {
        getTest().pass(logMessage);
        //Screenshot method
        attachScreenshot(driver,screenShotMessage);
    }

    //Log a step validation for Api
    public static void logStepValidationForApi( String logMessage) {
        getTest().pass(logMessage);
    }
    //Log a step validation with screenshot
    public static void logFailureAPI(WebDriver driver, String logMessage,String screenShotMessage) {
        getTest().pass(logMessage);
        //Screenshot method
        attachScreenshot(driver,screenShotMessage);
    }


    //Log a failure
    public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
        String colorMessage=String.format("<span style='color:red;'>%s</span>",logMessage);
        getTest().fail(colorMessage);
        //ScreenshotMethod
        attachScreenshot(driver,screenShotMessage);
    }
    //Log a failure for Api
    public static void logFailureApi(String logMessage) {
        String colorMessage=String.format("<span style='color:red;'>%s</span>",logMessage);
        getTest().fail(colorMessage);
    }

    //Log a Failure for API
    public static void logFailureAPI(String logMessage) {
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTest().fail(colorMessage);
    }

    //Log a skip
    public static void logSkip(String logMessage) {
        String colorMessage=String.format("<span style='color:orange;'>%s</span>",logMessage);
        getTest().skip(colorMessage);
        //ScreenshotMethod
    }
// Take screenshot with date and time

    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String destPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/screenshots/" + screenshotName + "_" + timeStamp + ".png";
        File finalPath = new File(destPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Convert screenshot to Base64 for embedding in the report
        String base64Format = convertTobase64(src);
        return base64Format;
    }

    private static String convertTobase64(File screenshotFile) {
        String base64Format = "";
        byte[] fileContent;
        try {
            fileContent = FileUtils.readFileToByteArray(screenshotFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return base64Format = Base64.getEncoder().encodeToString(fileContent);
    }
//Attach screenshot to report using Base 64
    public synchronized static void  attachScreenshot(WebDriver driver,String message) {
        try {
            String screenShotBase64 = takeScreenshot(driver, getTestName());
            getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (Exception e) {
            getTest().fail("Failed to attach screenshot:" + message);
            e.printStackTrace();
        }
    }
    public static void registerDriver(WebDriver driver) {
        driverMap.put(Thread.currentThread().getId(), driver);
    }
}