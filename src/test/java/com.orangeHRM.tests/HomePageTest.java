package com.orangeHRM.tests;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setUpPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyHRMlogo(String userName, String password) {
        // ExtentManager.startTest("Home Page Verify Logo Test");--This has been implemented in TestListener
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login("admin", "admin123");
        ExtentManager.logStep("Verifying OrangeHRM Logo is visible or not");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(), "Test failed: OrangeHRM logo is not visible");
        ExtentManager.logStep("Validation successful");
        homePage.logOut();
        ExtentManager.logStep("Logged out Successfully");
    }
}
