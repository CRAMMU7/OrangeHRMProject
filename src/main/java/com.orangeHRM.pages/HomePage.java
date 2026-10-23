package com.orangeHRM.pages;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private ActionDriver actionDriver;

    /*
        public HomePage(WebDriver driver) {
            this.actionDriver = new ActionDriver(driver);

        }*/
    public HomePage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();
    }

    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text='Logout']");
    private By orangeHRMLogo = By.cssSelector("div[class='oxd-brand-banner'] img");
    private By pimTab =By.xpath("//span[text()='PIM']");
    private By employeeSearch =By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card --mobile']/div/div/div[2]/div/div/div[2]");
    private By emplLastName = By.xpath("//div[@class='oxd-table-card --mobile']/div/div/div[2]/div[2]/div/div[2]");

    public boolean isAdminTabVisible() {
        return actionDriver.isDisplayed(adminTab);
    }

    public boolean verifyOrangeHRMLogo() {
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    //Method to navigate to PIM tab
    public void clickOnPIMTab(){
        actionDriver.click(pimTab);
    }

    //Employee Search
    public void employeeSearch(String value){
        actionDriver.enterText(employeeSearch,value);
        actionDriver.click(searchButton);
        actionDriver.scrollToElement(emplFirstAndMiddleName);
    }

    //Verify employee first and middle name
    public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
        return  actionDriver.compareText(emplFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
    }

    //Verify employee first and middle name
    public boolean verifyEmployeeLastName(String emplLastFromDB) {
        return  actionDriver.compareText(emplLastName, emplLastFromDB);
    }
    public void logOut() {
        actionDriver.click(userIdButton);
        actionDriver.click(logoutButton);

    }
}
