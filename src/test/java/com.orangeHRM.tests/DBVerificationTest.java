package com.orangeHRM.tests;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DBConnection;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class DBVerificationTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyEmployeeNameVerificationFromDB(String emplID, String empName) {
        SoftAssert softAssert = getSoftAssert();
        ExtentManager.logStep("click on PIM tab");
        homePage.clickOnPIMTab();

        ExtentManager.logStep("Search for Employee");
        homePage.employeeSearch(empName);


        ExtentManager.logStep("Get the Employee Name from DB");
        String employee_id = emplID;

        //Fetch the data into a map

        Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);

        String emplFirstName = employeeDetails.get("firstName");
        String emplMiddleName = employeeDetails.get("middleName");
        String emplLastName = employeeDetails.get("lastName");

        String emplFirstAndMiddleName = (emplFirstName + " " + emplMiddleName).trim();

        //Validation for first and middle name
        ExtentManager.logStep("Verify the employee first and middle name");
        softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName), "First and Middle name are not Matching");

        //validation for last name
        ExtentManager.logStep("Verify the employee last name");
        softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName));

        ExtentManager.logStep("DB Validation Completed");

        softAssert.assertAll();
    }
}
