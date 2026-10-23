package com.orangeHRM.actiondriver;

/*Action Driver class acts as a custom utility layer,designed to handle common browser interactions efficiently.It simplifies Webdriver functionalities
 by adding additional features like explicit waits ,error handling and reusuable code*/

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver {
    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger Logger = BaseClass.Logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicit_wait = Integer.parseInt(BaseClass.getProp().getProperty("explicit_wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicit_wait));
        System.out.println("Web Driver Instance is created");
    }

    //Wait for element to be clickable
    public void waitForElementToBeClickable(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    //Wait for elemnt to be visible
    public void waitForElementToBeVisible(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    //Method to click
    public void click(By by) {
        String elementDescription = getElementDescription(by);
        try {
            applyBorder(by,"green");
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("clicked an element");
            Logger.info("clicked an element-->" + elementDescription);
        } catch (Exception ex) {
            applyBorder(by,"red");
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:", elementDescription + "_unable to click");
            Logger.error("Unable to click element " + ex.getMessage());

        }
    }

    //Method to enter text into an input field
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by,"green");
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(value);
            Logger.info("Entered text on : " + getElementDescription(by) + "-->" + value);

        } catch (Exception e) {
            applyBorder(by,"red");
            Logger.error("Entered text: " + value);
        }
    }

    //Method to get text from input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by,"green");
            return driver.findElement(by).getText();
        } catch (Exception e) {
            applyBorder(by,"red");
            Logger.error("Unable to get value from the input box " + e.getMessage());
        }
        return "";
    }

    //compare 2 text
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (actualText.equals(expectedText)) {
                applyBorder(by,"green");
                Logger.info("Texts are matching " + actualText + " equals " + expectedText);
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "Text Verified successfully!:" + actualText + "equals " + expectedText);
                return true;
            } else {
                applyBorder(by,"red");
                Logger.error("Texts are not matching " + actualText + " not equals " + expectedText);
                ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison failed", "Text Comparison failed:" + actualText + " not equals " + expectedText);
                return false;
            }
        } catch (Exception e) {
            applyBorder(by,"red");
            Logger.error("Unable to compare Texts:" + e.getMessage());
        }
        return false;
    }

    public boolean isDisplayed(By by) {

        try {
            waitForElementToBeVisible(by);
            Logger.info("Element is Displayed: " + getElementDescription(by));
            ExtentManager.logStep("Element is Displayed: " + getElementDescription(by));
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            Logger.error("Element is not displayed:" + e.getMessage());
            ExtentManager.logFailure(BaseClass.getDriver(), "Elemnt is not sisplayed: ", "Elemnt is not displayed");
            return false;
        }
    }

    //Scroll to an element
    public void scrollToElement(By by) {
        try {
            WebElement element = driver.findElement(by);
            applyBorder(by,"green");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0],scrollIntoView(true)", element);
        } catch (Exception e) {
            applyBorder(by,"red");
            Logger.error("Unable to locate element:" + e.getMessage());
        }
    }

    //Wait for Page to load
    public void waitForPageLoad(int timeOutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
                    .executeScript("return.document.readyState").equals("complete"));
            Logger.info("Page Loaded successfully");
        } catch (Exception e) {
            Logger.error("Page did not load within " + timeOutInSec + " seconds.Exception" + e.getMessage());
        }
    }

    // Method to get the description of an element using By locator
    public String getElementDescription(By locator) {

        if (driver == null) {
            return "driver is not initialized";
        }
        if (locator == null) {
            return "Locator is not initialized";
        }

        try {
            WebElement element = driver.findElement(locator);

            String name = element.getDomProperty("name");
            String id = element.getDomProperty("id");
            String text = element.getText();
            String className = element.getDomProperty("class");
            String placeholder = element.getDomProperty("placeholder");
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with ID: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text" + truncate(text, 50);
            } else if (isNotEmpty(className)) {
                return "Element with className" + className;
            } else if (isNotEmpty(placeholder)) {
                return "Element with placeholder" + placeholder;
            } else {
                return "Element located using locator :" + locator.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to describe elemnt due to error" + e.getMessage();
        }
    }

    // Utility method to check if a string is not null or empty
    public boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // Utility method to truncate long strings
    public String truncate(String value, int maxLength) {
        if (value == null || value.length() < maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    //Utility Method to Border an element
    public void applyBorder(By by, String color) {
        try {
            //Locate the element
            WebElement element = driver.findElement(by);
            //Apply the border
            String script = "arguments[0].style.border='3px solid " + color + "'";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(script, element);
            Logger.info("Applied the border with color " + color + " to element " + getElementDescription(by));
        } catch (Exception e) {
            Logger.warn("Failed to apply the border to element " + getElementDescription(by), e);
        }
    }
    // ===================== Advanced WebElement Actions =====================

    public void moveToElement(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(by)).perform();
            ExtentManager.logStep("Moved to element: " + elementDescription);
            Logger.info("Moved to element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element", elementDescription + "_move_failed");
            Logger.error("Unable to move to element: " + e.getMessage());
        }
    }

    public void dragAndDrop(By source, By target) {
        String sourceDescription = getElementDescription(source);
        String targetDescription = getElementDescription(target);
        try {
            Actions actions = new Actions(driver);
            actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
            ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
            Logger.info("Dragged element: " + sourceDescription + " and dropped on " + targetDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to drag and drop", sourceDescription + "_drag_failed");
            Logger.error("Unable to drag and drop: " + e.getMessage());
        }
    }

    public void doubleClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.doubleClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Double-clicked on element: " + elementDescription);
            Logger.info("Double-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to double-click element", elementDescription + "_doubleclick_failed");
            Logger.error("Unable to double-click element: " + e.getMessage());
        }
    }

    public void rightClick(By by) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.contextClick(driver.findElement(by)).perform();
            ExtentManager.logStep("Right-clicked on element: " + elementDescription);
            Logger.info("Right-clicked on element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to right-click element", elementDescription + "_rightclick_failed");
            Logger.error("Unable to right-click element: " + e.getMessage());
        }
    }

    public void sendKeysWithActions(By by, String value) {
        String elementDescription = getElementDescription(by);
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(driver.findElement(by), value).perform();
            ExtentManager.logStep("Sent keys to element: " + elementDescription + " | Value: " + value);
            Logger.info("Sent keys to element --> " + elementDescription + " | Value: " + value);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to send keys", elementDescription + "_sendkeys_failed");
            Logger.error("Unable to send keys to element: " + e.getMessage());
        }
    }

    public void clearText(By by) {
        String elementDescription = getElementDescription(by);
        try {
            driver.findElement(by).clear();
            ExtentManager.logStep("Cleared text in element: " + elementDescription);
            Logger.info("Cleared text in element --> " + elementDescription);
        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to clear text", elementDescription + "_clear_failed");
            Logger.error("Unable to clear text in element: " + e.getMessage());
        }
    }

    // Method to upload a file
    public void uploadFile(By by, String filePath) {
        try {
            driver.findElement(by).sendKeys(filePath);
            applyBorder(by, "green");
            Logger.info("Uploaded file: " + filePath);
        } catch (Exception e) {
            applyBorder(by, "red");
            Logger.error("Unable to upload file: " + e.getMessage());
        }
    }

}



