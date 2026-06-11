package com.dotlab.utils;

import com.dotlab.tests.CourseTestingSuite;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.File;
import java.io.IOException;

public class TestListener implements ITestListener {
    
    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver = ((CourseTestingSuite) testClass).getDriver();
        
        if (driver != null) {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotName = result.getName() + "_" + System.currentTimeMillis() + ".png";
            try {
                FileUtils.copyFile(srcFile, new File("./screenshots/" + screenshotName));
                System.out.println("[FAILURE TIMEOUT] Screen layout captured: " + screenshotName);
            } catch (IOException e) {
                System.out.println("[WARN] Failed to write screenshot image: " + e.getMessage());
            }
        }
    }
}