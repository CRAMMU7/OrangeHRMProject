package com.orangeHRM.tests;

import com.orangeHRM.base.BaseClass;
import org.testng.annotations.Test;
import org.testng.Assert;

public class DummyTestClass extends BaseClass {


    @Test
    public void dummyTest() {
        String title = getDriver().getTitle();
        Assert.assertEquals(title, "OrangeHRM", "Title is not matching");
        System.out.println("Title is matching");
    }
}

