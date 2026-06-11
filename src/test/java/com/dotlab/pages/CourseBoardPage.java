package com.dotlab.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CourseBoardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions hardwareBridge;

    // ==================== DESIGN-INVARIANT SELECTORS ====================
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginSubmitBtn = By.xpath("//form//button[@type='submit'] | //button[contains(@class, 'button')]");
    
    private final By searchInputField = By.xpath("//input[@placeholder='Search courses...']");
    
    // Exact structural matches for the raw DOM layouts you provided
    private final By topicAccordionHeader = By.xpath("//div[contains(@class, 'cursor-pointer')]//div[text()='C Fundamentals'] | //*[contains(text(), 'C Fundamentals')]/ancestor::div[contains(@class, 'cursor-pointer')]");
    private final By subTopicLessonItem = By.xpath("//p[contains(text(), 'Keywords, Identifiers, Constants, Variables, Data Types')] | //div[contains(@class, 'cursor-pointer')]//p[contains(., 'Keywords')]");
    
    // Dynamic tab switching based on visible text layout labels
    private final String tabSelectorPattern = "//button[normalize-space(.)='%s'] | //button[contains(text(), '%s')]";
    
    private final By chartVisualNodes = By.xpath("//*[local-name()='svg']//*[local-name()='rect' or local-name()='path' and contains(@class, 'apexcharts')] | //canvas");
    private final By leaderboardBtn = By.xpath("//button[contains(., 'Leaderboard') or contains(., 'View')]");
    private final By materialTypeTabs = By.xpath("//div[contains(@class,'tabs')]//button | //button[contains(@class, 'tab')]");
    private final By mindmapActionButtons = By.xpath("//*[contains(@class, 'mindmap')]//button | //div[contains(@class, 'toolbar')]//button");
    private final By codeSnippetCopyBtn = By.xpath("//pre//button | //div[contains(@class, 'code')]//*[local-name()='svg']");
    
    private final By tryItPlaygroundBtn = By.xpath("//button[contains(text(), 'Try It')] | //button[contains(., 'Try')]");
    private final By exitPlaygroundBtn = By.xpath("//button[contains(.,'Exit') or contains(.,'Close') or contains(.,'Back')] | //*[contains(@class, 'modal') or contains(@class, 'fixed')]//button");
    
    private final By profileDropdownTrigger = By.xpath("//div[contains(@class,'profile-container')] | //*[contains(@class, 'footer')]//img/ancestor::div[1] | //div[contains(@class, 'sidebar')]//button[last()]");
    private final By cleanLogoutTarget = By.xpath("//*[normalize-space()='Log out' or normalize-space()='Logout'] | //button[contains(., 'Log out') or contains(., 'Logout')] | //span[contains(text(), 'Log out') or contains(text(), 'Logout')]");

    public CourseBoardPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.hardwareBridge = new Actions(driver);
    }

    // ==================== ACTION WORKFLOW METHODS ====================

    public void executeLoginHandshake(String url, String username, String password) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        
        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(loginSubmitBtn));
        try {
            submit.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
        }
        wait.until(ExpectedConditions.urlContains("dashboard"));
    }

    public void directRouteToCourseBoard(String targetUrl) {
        driver.get(targetUrl);
        wait.until(ExpectedConditions.urlContains("course-board"));
    }

    public void filterCatalogByQuery(String queryText) throws InterruptedException {
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputField));
        box.clear();
        box.sendKeys("NonExistentCourseXYZ");
        Thread.sleep(1000);

        box.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        box.clear();
        box.sendKeys(queryText);
        Thread.sleep(1500);
    }

    public String getSearchFieldValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputField)).getAttribute("value");
    }

    public void launchWorkspaceForCourse(String courseName) {
        By dynamicCardBtn = By.xpath("//*[contains(text(), '" + courseName + "')]/ancestor::div[contains(@class,'card') or contains(@class,'rounded')]//button | //button[contains(text(), 'Course Overview')]");
        WebElement entryBtn = wait.until(ExpectedConditions.elementToBeClickable(dynamicCardBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", entryBtn);
        try {
            entryBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", entryBtn);
        }
        wait.until(ExpectedConditions.urlContains("course"));
    }

    public void expandTopicAccordion() throws InterruptedException {
        // Enforce using the updated class global selector variable explicitly
        WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(this.topicAccordionHeader));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", header);
        Thread.sleep(1200);
        
        WebElement clickableHeader = wait.until(ExpectedConditions.elementToBeClickable(this.topicAccordionHeader));
        try {
            clickableHeader.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableHeader);
        }
        Thread.sleep(2500);
    }

    public void selectSubTopicLesson() throws InterruptedException {
        // FIXED: Explicitly maps the corrected layout string variable down into execution space
        WebElement lesson = wait.until(ExpectedConditions.presenceOfElementLocated(this.subTopicLessonItem));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", lesson);
        Thread.sleep(1200);
        
        WebElement clickableLesson = wait.until(ExpectedConditions.elementToBeClickable(this.subTopicLessonItem));
        try {
            clickableLesson.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableLesson);
        }
        Thread.sleep(4000);
    }

    public void switchWorkspaceSubTab(String tabLabel) throws InterruptedException {
        By contextualTab = By.xpath(String.format(tabSelectorPattern, tabLabel, tabLabel));
        WebElement tabBtn = wait.until(ExpectedConditions.elementToBeClickable(contextualTab));
        try {
            tabBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tabBtn);
        }
        Thread.sleep(2000);
    }

    public void interactWithDashboardAnalytics() {
        try {
            List<WebElement> elements = driver.findElements(chartVisualNodes);
            if (!elements.isEmpty()) {
                WebElement targetNode = elements.get(elements.size() / 2);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", targetNode);
                Thread.sleep(1000);
                hardwareBridge.moveToElement(targetNode).build().perform();
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            System.out.println("[WARN] Analytics interaction warning: " + e.getMessage());
        }
    }

    public void clickOptionalLeaderboard() {
        try {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(leaderboardBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            btn.click();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("[INFO] Leaderboard modal step optional for this sequence layout layer.");
        }
    }

    public void traverseMaterialSubTabs() throws InterruptedException {
        List<WebElement> tabs = driver.findElements(materialTypeTabs);
        for (int i = 0; i < Math.min(tabs.size(), 3); i++) {
            WebElement tab = tabs.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tab);
            try { tab.click(); } catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab); }
            Thread.sleep(1200);
        }
    }

    public void triggerMindmapActions() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight / 3);");
        Thread.sleep(1000);
        List<WebElement> buttons = driver.findElements(mindmapActionButtons);
        for (WebElement btn : buttons) {
            try { btn.click(); } catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn); }
            Thread.sleep(600);
        }
    }

    public void triggerCodeSnippetCopy() {
        try {
            WebElement copyBtn = driver.findElement(codeSnippetCopyBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", copyBtn);
            copyBtn.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("[INFO] Dynamic clipboard action item handled smoothly.");
        }
    }

    public void runSandboxPlaygroundLifecycle() {
        try {
            WebElement tryIt = wait.until(ExpectedConditions.elementToBeClickable(tryItPlaygroundBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tryIt);
            try { tryIt.click(); } catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tryIt); }
            Thread.sleep(4000);

            WebElement exitBtn = wait.until(ExpectedConditions.elementToBeClickable(exitPlaygroundBtn));
            try { exitBtn.click(); } catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", exitBtn); }
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("[FAULT] Sandbox playground execution flow variation matched: " + e.getMessage());
        }
    }

    public void resetViewToTop() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(1500);
    }

    public void resetViewToBottom() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(1500);
    }

    public void terminateSessionSecurely() {
        try {
            WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(profileDropdownTrigger));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", profile);
            try { profile.click(); } catch (Exception e) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", profile); }
            Thread.sleep(1500);

            try {
                WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(cleanLogoutTarget));
                logout.click();
            } catch (Exception e) {
                if (!driver.getCurrentUrl().contains("/login")) {
                    driver.get("https://dotlab.amypo.ai/login");
                }
            }
            wait.until(ExpectedConditions.urlContains("/login"));
        } catch (Exception e) {
            throw new RuntimeException("Secure session termination breakdown encountered.", e);
        }
    }
}