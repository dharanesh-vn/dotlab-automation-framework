package com.dotlab.utils;

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
        WebDriver driver = null;
        
        // 1. Try to fetch the decoupled driver instance from the TestNG Context attributes
        if (result.getTestContext() != null) {
            driver = (WebDriver) result.getTestContext().getAttribute("WebDriver");
        }
        
        // 2. Fallback: Try reflection if the context attribute wasn't assigned properly
        if (driver == null) {
            try {
                Object testClass = result.getInstance();
                java.lang.reflect.Method getDriverMethod = testClass.getClass().getMethod("getDriver");
                driver = (WebDriver) getDriverMethod.invoke(testClass);
            } catch (Exception e) {
                System.out.println("[WARN] Reflection lookup fallback failed to find an active WebDriver context instance.");
            }
        }
        
        // 3. Process the layout capture safely if an active driver session exists
        if (driver != null) {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String screenshotName = result.getName() + "_" + System.currentTimeMillis() + ".png";
            try {
                File targetDestination = new File("./screenshots/" + screenshotName);
                FileUtils.copyFile(srcFile, targetDestination);
                System.out.println("[FAILURE TIMEOUT] Screen layout captured: " + targetDestination.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("[WARN] Failed to write screenshot image: " + e.getMessage());
            }
        } else {
            System.out.println("[WARN] Driver instance was null. Skipping automatic layout screenshot grab.");
        }
    }
}