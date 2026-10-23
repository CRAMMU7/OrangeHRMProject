package com.orangeHRM.pages;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionDriver actionDriver;

    /*
    public LoginPage(WebDriver driver ){
        this.actionDriver = new ActionDriver(driver);
    }

*/
    public LoginPage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();
    }

    private By userNameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    //method to perform login
    public void login(String username, String password) {
        actionDriver.enterText(userNameField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }

    //Method to check if error message Displayed
    public boolean isErrorMessageDispalyed() {
        return actionDriver.isDisplayed(errorMessage);
    }

    //Method to get Text From Error Message
    public String getErrorMessageText() {
        return actionDriver.getText(errorMessage);
    }

    //Verify if error is correct or not
    public boolean verifyErrorMessage(String expectedError) {
        return actionDriver.compareText(errorMessage, expectedError);
        
    }
}
