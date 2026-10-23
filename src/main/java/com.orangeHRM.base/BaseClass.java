package com.orangeHRM.base;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseClass {

    //protected static WebDriver driver;
//private static ActionDriver actionDriver;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
    protected ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();
    protected static Properties prop;
    public static final Logger Logger = LoggerManager.getLogger(BaseClass.class);

    public SoftAssert getSoftAssert() {
        return softAssert.get();
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/resources/config.properties");

        prop.load(fis);
        Logger.info("config.properties file is loaded");

        //Start the extent report
        // ExtentManager.getReporter();--This has been implemented in ITEstListener
    }

    @BeforeMethod
    public synchronized void setUp() {
        System.out.println("Setting up WebDriver for :" + this.getClass().getSimpleName());
        launchBrowser();
        configBrowser();

        Logger.info("Webdriver initialized amd Browser maximised");
        Logger.trace("Trace message");
        Logger.error("Error message");
        Logger.debug("debug message");
        /*
        //Initialize ActionDriver only once
        if (actionDriver == null) {
            actionDriver = new ActionDriver(driver);

            Logger.info("ActionDriver instance is created");
        }
         */
        actionDriver.set(new ActionDriver(getDriver()));
        Logger.info("ActionDriver initialized for thread: " + Thread.currentThread().getName());
    }


    private synchronized void launchBrowser() {
        String browser = prop.getProperty("browser");

        if ("chrome".equalsIgnoreCase(browser)) {
            // driver = new ChromeDriver();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Run Chrome in headless mode
            options.addArguments("--disable-gpu"); // Disable GPU for headless mode
            //options.addArguments("--window-size=1920,1080"); // Set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

            driver.set(new ChromeDriver());
            ExtentManager.registerDriver(getDriver());
            Logger.info("ChromeDriver instance is created");
        } else if ("edge".equalsIgnoreCase(browser)) {
            // driver = new EdgeDriver();

            driver.set(new EdgeDriver());
            ExtentManager.registerDriver(getDriver());
        } else if ("firefox".equalsIgnoreCase(browser)) {
            //  driver = new FirefoxDriver();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless"); // Run Chrome in headless mode
            options.addArguments("--disable-gpu"); // Disable GPU for headless mode
            //options.addArguments("--window-size=1920,1080"); // Set window size
            options.addArguments("--disable-notifications"); // Disable browser notifications
            options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
            options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

            driver.set(new FirefoxDriver());
            ExtentManager.registerDriver(getDriver());
        } else {
            throw new IllegalArgumentException("Invalid browser: " + browser);
        }
    }

    private void configBrowser() {
        int implicitWait = Integer.parseInt(prop.getProperty("implicit_wait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().window().maximize();
        try {
            getDriver().get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to navigate to the URL: " + e.getMessage());
        }
    }

    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("Unable to quit the driver " + e.getMessage());
            }
        }
        Logger.info("WebDriver instance is closed");

        driver.remove();
        actionDriver.remove();
        //  ExtentManager.endTest(); ---implemented in Listener class
        System.out.println("Flushing report...");
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            System.out.println("WedDriver is not initialized");
            throw new IllegalStateException("WedDriver is not initialized");
        }
        return driver.get();
    }

    public static ActionDriver getActionDriver() {
        if (actionDriver.get() == null) {
            System.out.println("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

    public void setDriver(ThreadLocal<WebDriver> driver) {
        this.driver = driver;
    }

    //getter method for prop
    public static Properties getProp() {
        return prop;
    }
}
