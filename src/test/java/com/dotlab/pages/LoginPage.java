package com.dotlab.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailInputField = By.id("email");
    private final By passwordInputField = By.id("password");
    private final By loginSubmitButton = By.xpath("//button[@type='submit'] | //button[contains(., 'Sign In') or contains(., 'Login')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void loginToPlatform(String userEmail, String userPassword) {
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInputField));
        email.clear();
        email.sendKeys(userEmail);

        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInputField));
        password.clear();
        password.sendKeys(userPassword);

        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(loginSubmitButton));
        try {
            submit.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
        }
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }
}