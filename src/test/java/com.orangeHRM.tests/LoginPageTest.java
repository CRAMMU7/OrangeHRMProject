package com.orangeHRM.tests;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());

    }

    @Test(dataProvider = "validLoginData",dataProviderClass =  DataProviders.class)
    public void verifyValidLoginTest(String userName, String password) {
   //     ExtentManager.startTest("Valid Login Test");--This has been implemented in TestListener
        System.out.println("Running testMethod1 on thread : "+Thread.currentThread().getId());
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login(userName, password);
        ExtentManager.logStep("Verifying admin tab is visible or not");
        Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successful login");
        ExtentManager.logStep("Validation successful");
        homePage.logOut();
        ExtentManager.logStep("Logged out successfully");

    }

    @Test(dataProvider = "inValidLoginData",dataProviderClass =  DataProviders.class)
    public void invalidLoginTest(String userName,String password){
    //    ExtentManager.startTest("In-valid Login Test");--This has been implemented in TestListener
        System.out.println("Running testMethod1 on thread : "+Thread.currentThread().getId());
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login(userName, password);
        String expectedErrorMessage = "Invalid credentials";
        ExtentManager.logStep("Verifying admin tab is visible or not");
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test failed:Invalid error message");
        ExtentManager.logStep("Validation successful");
    }
}
