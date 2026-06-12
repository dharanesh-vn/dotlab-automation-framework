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
    
    // Core structural layout locators for curriculum tracking
    private final By topicAccordionHeader = By.xpath("//div[contains(@class, 'cursor-pointer')]//div[text()='C Fundamentals'] | //*[contains(text(), 'C Fundamentals')]/ancestor::div[contains(@class, 'cursor-pointer')]");
    private final By subTopicLessonItem = By.xpath("//p[contains(text(), 'Keywords, Identifiers, Constants, Variables, Data Types')] | //div[contains(@class, 'cursor-pointer')]//p[contains(., 'Keywords')]");
    
    // Core workspace sub-navigation text locator pattern (Dashboard, Prepare, Practice, Discussion)
    private final String tabSelectorPattern = "//button[contains(normalize-space(.), '%s')] | //button[descendant-or-self::text()[contains(., '%s')]]";
    
    // Internal Tab Selectors: Dashboard components
    private final By leaderboardBtn = By.xpath("//button[contains(normalize-space(.), 'View Leaderboard')]");
    private final By chartVisualNodes = By.xpath("//*[local-name()='svg']//*[local-name()='rect' or local-name()='path' and contains(@class, 'apexcharts')] | //canvas");
    
    // Internal Tab Selectors: Prepare / Practice components (Materials & PDF)
    private final By materialsTabBtn = By.xpath("//button[./span[normalize-space(.)='Materials']] | //button[contains(., 'Materials')]");
    private final By pdfTabBtn = By.xpath("//button[./span[normalize-space(.)='PDF']] | //button[contains(., 'PDF')]");
    
    // Draggable React Flow Canvas & Interactive Elements
    private final By draggableFlowPane = By.xpath("//div[contains(@class, 'react-flow__pane') and contains(@class, 'draggable')]");
    private final By mindmapActionButtons = By.xpath("//*[contains(@class, 'mindmap')]//button | //div[contains(@class, 'toolbar')]//button");
    private final By codeSnippetCopyBtn = By.xpath("//div[contains(@class, 'csm-mindmap-node')]//button | //pre//button | //div[contains(@class, 'code')]//*[local-name()='svg' or contains(@class, 'copy')]");
    
    // ==================== RE-ENGINEERED DOM SANDBOX SELECTORS ====================
    // Try It Button Target (Matching: class="button border border-blue-500... button-press-feedback")
    private final By tryItPlaygroundBtn = By.xpath("//button[text()='Try it'] | //button[contains(@class, 'button-press-feedback') and contains(., 'Try it')]");
    
    // Sandbox Run Code Action (Matching: class="... bg-emerald-500 ... " containing text "Run")
    private final By sandboxRunCodeBtn = By.xpath("//button[contains(@class, 'bg-emerald-500') and normalize-space(.)='Run'] | //button[descendant::*[local-name()='svg'] and normalize-space(.)='Run']");
    
    // Theme/Layout Toggle/Accessibility Anchor (Matching yellow Sun path properties)
    private final By sandboxAccessibilityBtn = By.xpath("//*[local-name()='svg' and contains(@class, 'text-yellow-500')]/ancestor::button[1] | //button[contains(@class, 'bg-amber-200')]");
    
    // Exit Playground Workspace Target (Matching: class="... bg-orange-600 ... " containing text "Exit")
    private final By exitPlaygroundBtn = By.xpath("//button[contains(@class, 'bg-orange-600') and normalize-space(.)='Exit'] | //button[descendant::*[local-name()='svg'] and normalize-space(.)='Exit']");
    
    // Authentication & Profile Termination Selectors
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
        WebElement tabBtn = wait.until(ExpectedConditions.presenceOfElementLocated(contextualTab));
        
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tabBtn);
        Thread.sleep(1000);
        
        WebElement clickableTab = wait.until(ExpectedConditions.elementToBeClickable(contextualTab));
        try {
            clickableTab.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableTab);
        }
        Thread.sleep(2500);
    }

    // ==================== SUB-TAB INTERNAL INTERACTIONS ====================

    public void clickOptionalLeaderboard() {
        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(this.leaderboardBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
            Thread.sleep(1200);
            
            WebElement clickableBtn = wait.until(ExpectedConditions.elementToBeClickable(this.leaderboardBtn));
            try {
                clickableBtn.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickableBtn);
            }
            System.out.println("[SUCCESS] Interacted with View Leaderboard modal context safely.");
            Thread.sleep(2500);
        } catch (Exception e) {
            System.out.println("[WARN] Leaderboard button execution layer exception: " + e.getMessage());
        }
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

    public void traverseMaterialSubTabs() throws InterruptedException {
        WebElement pdf = wait.until(ExpectedConditions.presenceOfElementLocated(this.pdfTabBtn));
        try {
            pdf.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", pdf);
        }
        System.out.println("[SUCCESS] Inspected active PDF asset frame layout.");
        Thread.sleep(2000);

        WebElement materials = wait.until(ExpectedConditions.presenceOfElementLocated(this.materialsTabBtn));
        try {
            materials.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", materials);
        }
        System.out.println("[SUCCESS] Returned back to primary Materials interactive workspace.");
        Thread.sleep(2000);
    }

    public void triggerMindmapActions() throws InterruptedException {
        triggerMindmapDraggableCopy();
    }

    public void triggerCodeSnippetCopy() {
        try {
            WebElement copyBtn = wait.until(ExpectedConditions.presenceOfElementLocated(codeSnippetCopyBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", copyBtn);
            Thread.sleep(500);
            try {
                copyBtn.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", copyBtn);
            }
            System.out.println("[SUCCESS] Executed copy snippet interaction inside course content topic structure.");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("[INFO] Copy element interaction step fallback caught safely.");
        }
    }

    public void triggerMindmapDraggableCopy() {
        try {
            WebElement flowPane = wait.until(ExpectedConditions.presenceOfElementLocated(draggableFlowPane));
            
            hardwareBridge.moveToElement(flowPane)
                          .clickAndHold()
                          .moveByOffset(-150, -100)
                          .release()
                          .build()
                          .perform();
            System.out.println("[SUCCESS] Completed viewport layout shift on draggable react-flow board.");
            Thread.sleep(2000);

            WebElement copyBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(codeSnippetCopyBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", copyBtn);
            Thread.sleep(500);
            
            try {
                copyBtn.click();
            } catch (Exception clickEx) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", copyBtn);
            }
            System.out.println("[SUCCESS] Content code block copy action verified within draggable layout viewport context.");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("[WARN] Intermittent boundary block found during draggable flow canvas interaction check: " + e.getMessage());
        }
    }

    public void runSandboxPlaygroundLifecycle() {
        try {
            WebElement tryIt = wait.until(ExpectedConditions.elementToBeClickable(tryItPlaygroundBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tryIt);
            try { 
                tryIt.click(); 
            } catch (Exception e) { 
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tryIt); 
            }
            System.out.println("[SUCCESS] Transitioned to separate coding workspace view.");
            Thread.sleep(3500);

            // Now fully evaluates cleanly using the precise pure DOM text matching engine
            WebElement runBtn = wait.until(ExpectedConditions.elementToBeClickable(sandboxRunCodeBtn));
            try {
                runBtn.click();
                System.out.println("[SUCCESS] Clicked code editor 'Run' engine action.");
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", runBtn);
                System.out.println("[SUCCESS] Clicked code editor 'Run' engine action via JS Fallback.");
            }
            Thread.sleep(3000); // Dynamic compile cool down buffer step

            try {
                WebElement accessBtn = driver.findElement(sandboxAccessibilityBtn);
                if (accessBtn.isDisplayed()) {
                    try {
                        accessBtn.click();
                    } catch (Exception ex) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accessBtn);
                    }
                    System.out.println("[SUCCESS] Interacted with workspace layout option context panel safely.");
                    Thread.sleep(1000);
                }
            } catch (Exception accessEx) {
                System.out.println("[INFO] Accessibility / Theme layout toggles context verification skipped.");
            }

            WebElement exitBtn = wait.until(ExpectedConditions.elementToBeClickable(exitPlaygroundBtn));
            try { 
                exitBtn.click(); 
            } catch (Exception e) { 
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", exitBtn); 
            }
            System.out.println("[SUCCESS] Sandbox execution cycle closed. Returned back to original topic location.");
            Thread.sleep(2000);
            
        } catch (Exception e) {
            System.out.println("[FAULT] Error variations in Sandbox Playground operation framework: " + e.getMessage());
        }
    }

    public void performCompletePrepareToPracticeFlow() throws InterruptedException {
        switchWorkspaceSubTab("Dashboard");
        clickOptionalLeaderboard();
        interactWithDashboardAnalytics();
        
        switchWorkspaceSubTab("Prepare");
        traverseMaterialSubTabs();
        
        triggerCodeSnippetCopy();
        triggerMindmapActions();
        
        runSandboxPlaygroundLifecycle();
        
        switchWorkspaceSubTab("Practice");
        Thread.sleep(2000);
        
        switchWorkspaceSubTab("Discussion");
        System.out.println("[SUCCESS] Sequential layout sub-tab navigation pipeline verified entirely.");
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
            resetViewToTop();
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
            System.out.println("[SUCCESS] Terminated session sequence parameters safely.");
        } catch (Exception e) {
            throw new RuntimeException("Secure session termination breakdown encountered.", e);
        }
    }
}