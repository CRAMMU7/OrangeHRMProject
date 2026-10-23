package com.orangeHRM.tests;

import com.orangeHRM.utilities.ApiUtility;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ApiTest {

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void getUserApi(){
        SoftAssert softAssert = new SoftAssert();
        //Step1:Define APi endpoint
        String endPoint = "https://jsonplaceholder.typicode.com/users/1";
        ExtentManager.logStep("API Endpoint: " + endPoint);

        // Step2: Send GET Request
        ExtentManager.logStep("Sending GET Request to the API");
        Response response = ApiUtility.sendGetRequest(endPoint);

        // Step3: validate status code
        ExtentManager.logStepValidationForApi("Validating API Response status code");
       boolean isStatusCodeValid= ApiUtility.validateStatusCode(response,201);
        if (isStatusCodeValid) {
            ExtentManager.logStepValidationForApi("Status Code  Validation Passed!");
        } else {
            ExtentManager.logFailureAPI("isStatusCodeValid Validation Failed!");
        }
        softAssert.assertTrue(isStatusCodeValid,"Status code is not as Epxected");


        // Step4: validate username
        ExtentManager.logStep("Validating API Response for username ");
        String userName=ApiUtility.getJsonValue(response,"username");
        boolean isUserNameValid="Bret".equals(userName);
        softAssert.assertTrue(isStatusCodeValid,"User name is not valid");
        if (isUserNameValid) {
            ExtentManager.logStepValidationForApi("Username Validation Passed!");
        } else {
            ExtentManager.logFailureAPI("Username Validation Failed!");
        }

        // Step4: validate email
        ExtentManager.logStep("Validating response body for email");
        String userEmail = ApiUtility.getJsonValue(response, "email");
        boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
        softAssert.assertTrue(isEmailValid, "Email is not valid");
        if (isEmailValid) {
            ExtentManager.logStepValidationForApi("Email Validation Passed!");
        } else {
            ExtentManager.logFailureAPI("Email Validation Failed!");
        }

softAssert.assertAll();
    }
}
