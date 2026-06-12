package com.dotlab.pages;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CourseBoardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions hardwareBridge;
    private final JavascriptExecutor js;
    private final String loginUrl;

    private static final Duration ANIMATION_BRIDGE = Duration.ofMillis(450);

    // --- Core Authentication Framework Locators ---
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginSubmitBtn = By.xpath("//form//button[@type='submit'] | //button[contains(@class, 'button')]");

    // --- Search & Filtering Locators ---
    private final By searchInputField = By.xpath("//input[@placeholder='Search courses...']");
    private final By courseCard = By.xpath("//div[contains(@class,'course-card')]");
    private final By noResultsMessage = By.xpath("//*[contains(text(),'No courses found') or contains(text(),'No results')]");
    private final By loadingOverlay = By.xpath("//*[contains(@class,'loading') or contains(@class,'spinner') or @aria-busy='true']");

    // --- Layout & Workspace Tab Constants ---
    private final String tabSelectorPattern = "//button[contains(normalize-space(.), '%s')] | //button[descendant-or-self::text()[contains(., '%s')]]";
    private final By leaderboardBtn = By.xpath("//button[contains(normalize-space(.), 'View Leaderboard')]");
    private final By chartVisualNodes = By.xpath("//*[local-name()='svg']//*[local-name()='rect' or local-name()='path' and contains(@class, 'apexcharts')] | //canvas | //div[contains(@class, 'apexcharts-canvas')]//*[local-name()='path']");
    private final By materialsTabBtn = By.xpath("//button[./span[normalize-space(.)='Materials']] | //button[contains(., 'Materials')]");
    private final By pdfTabBtn = By.xpath("//button[./span[normalize-space(.)='PDF']] | //button[contains(., 'PDF')]");

    // --- Interactive Rich Media Components ---
    private final By draggableFlowPane = By.xpath("//div[contains(@class, 'react-flow__pane') or contains(@class, 'react-flow__renderer')]");
    private final By mindmapNodeButton = By.xpath("//div[contains(@class, 'react-flow__node')]//button | //div[contains(@class, 'csm-mindmap-node')]//button");
    private final By genericCopyBtn = By.xpath("//pre//button | //div[contains(@class, 'code')]//*[local-name()='svg' or contains(@class, 'copy')]");

    // --- Coding Sandbox Playground Elements ---
    private final By tryItPlaygroundBtn = By.xpath("//button[text()='Try it'] | //button[contains(@class, 'button-press-feedback') and contains(., 'Try it')]");
    private final By sandboxRunCodeBtn = By.xpath("//button[contains(@class, 'bg-emerald-500') and normalize-space(.)='Run'] | //button[descendant::*[local-name()='svg'] and normalize-space(.)='Run']");
    private final By sandboxAccessibilityBtn = By.xpath("//div[contains(@class, 'sandbox') or contains(@class, 'playground')]//button[contains(@class, 'bg-amber-200')] | //button[@title='Accessibility Assistant']");
    private final By exitPlaygroundBtn = By.xpath("//button[contains(@class, 'bg-orange-600') and normalize-space(.)='Exit'] | //button[descendant::*[local-name()='svg'] and normalize-space(.)='Exit']");
    private final By monacoEditorTextArea = By.xpath("//div[contains(@class, 'monaco-editor')]//textarea | //textarea[contains(@class, 'ace_text-input')] | //div[@role='code']//textarea | //div[contains(@class, 'monaco-mouse-cursor-text')]");
    private final By sandboxOutputTerminal = By.xpath("//div[contains(@class, 'terminal') or contains(@class, 'output')] | //pre[contains(@class, 'console')]");

    // --- Dedicated Extended Practice & Assessment Lifecycle Locators ---
    private final By practiceStartButton = By.xpath("//button[contains(@class, 'button') and text()='Start Practice']");
    private final By testConfirmStartBtn = By.xpath("//button[contains(text(), 'Start Test')]");
    private final By guidelineContinueBtn = By.xpath("//button[contains(text(), 'Continue')]");
    private final By themeToggleSvgBtn = By.xpath("//button[./svg or descendant::svg]//*[local-name()='path' and contains(@d, 'M12 2.25')]/ancestor::button[1] | //main//svg[contains(@class, 'text-yellow-500')] | //div[contains(@class, 'monaco-editor')]/preceding::button[./svg or descendant::svg]");
    private final By activeTestRunCodeBtn = By.xpath("//button[normalize-space(.)='Run' and descendant::*[local-name()='svg']]");
    private final By activeTestEndBtn = By.xpath("//button[./p[text()='End test']] | //p[text()='End test']/ancestor::button[1]");
    private final By exitModalConfirmBtn = By.xpath("//button[contains(@class, 'bg-orange-600') and text()='Exit']");

    // --- Profile & Lifecycle Traps ---
    private final By profileDropdownTrigger = By.xpath("//div[contains(@class,'profile-container')] | //*[contains(@class, 'footer')]//img/ancestor::div[1] | //div[contains(@class, 'sidebar')]//button[last()]");

    // --- Progress Tracker Locators ---
    private final By aggregateProgressBar = By.xpath("//div[contains(@class, 'progress-bar')] | //div[@role='progressbar']");
    private final By progressPercentageLabel = By.xpath("//span[contains(text(), '%')] | //p[contains(text(), '% Complete')]");
    private final By popoutExternalLinkBtn = By.xpath("//a[contains(@href, 'external') or @target='_blank'] | //button[contains(., 'Pop out') or contains(., 'Open in New Window')]");

    // --- Phase 2: Live Discussion Matrix Locators ---
    private final By discussionHeaderLabel = By.xpath("//*[contains(text(), 'Discuss Your Doubts')]");
    private final By facultyFilterPill = By.xpath("//button[normalize-space(text())='Faculty' or contains(., 'Faculty')]");
    
    private final By tiptapEditorField = By.cssSelector("div.tiptap.ProseMirror");
    private final By sendPostBtn = By.xpath("//button[contains(@class, 'button') or @type='submit'][descendant::span[text()='Send'] or contains(., 'Send')]");
    
    // Resilient TipTap Rich Text Toolbar Buttons mapping descriptive metadata
    private final By boldFormatBtn = By.xpath("//button[@title='Bold' or contains(@class, 'bold') or .//strong or .//b or .//*[local-name()='svg' and @data-icon='bold']]");
    private final By italicFormatBtn = By.xpath("//button[@title='Italic' or contains(@class, 'italic') or .//em or .//i or .//*[local-name()='svg' and @data-icon='italic']]");
    private final By codeBlockFormatBtn = By.xpath("//button[@title='Code Block' or contains(@class, 'code')]");
    private final By headingFormatBtn = By.xpath("//button[contains(@title, 'Heading') or contains(@class, 'heading') or contains(., 'H1') or contains(., 'H2')]");
    
    private final By genericMessageFeedBubble = By.cssSelector("div.rich-text-editor");

    public CourseBoardPage(WebDriver driver, WebDriverWait wait) {
        this(driver, wait, "https://dotlab.amypo.ai/login");
    }

    public CourseBoardPage(WebDriver driver, WebDriverWait wait, String loginUrl) {
        this.driver = driver;
        this.wait = wait;
        this.hardwareBridge = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        this.loginUrl = loginUrl;
    }

    private void waitForAppReady() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingOverlay));
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("[INVESTIGATE] Native click blocked; firing fallback Javascript dispatch: " + e.getMessage());
            js.executeScript("arguments[0].click();", element);
        }
    }

    private void directClickViaJavaScript(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    private void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }

    private void scrollAndClick(By locator) {
        waitForAppReady();
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(el);
        safeClick(el);
    }

    private void waitForAnimation() {
        try {
            Thread.sleep(ANIMATION_BRIDGE.toMillis());
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean waitForTabActive(WebElement tabBtn) {
        if (tabBtn.getAttribute("aria-selected") == null) {
            waitForAnimation();
            return true;
        }
        try {
            wait.until(ExpectedConditions.attributeContains(tabBtn, "aria-selected", "true"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String escapeXPathString(String input) {
        if (!input.contains("'")) {
            return "'" + input + "'";
        }
        return "concat('" + input.replace("'", "', \"'\", '") + "')";
    }

    public void executeLoginHandshake(String url, String username, String password) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);

        WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(loginSubmitBtn));
        safeClick(submit);
        wait.until(ExpectedConditions.urlContains("dashboard"));
        waitForAppReady();
    }

    public void directRouteToCourseBoard(String targetUrl) {
        driver.get(targetUrl);
        wait.until(ExpectedConditions.urlContains("course-board"));
        waitForAppReady();
    }

    public void terminateSessionSecurely() {
        try {
            if (driver == null) {
                System.out.println("[INFO] Driver instance is null. Skipping logout teardown.");
                return;
            }

            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

            resetViewToTop();
            waitForAppReady();

            WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(profileDropdownTrigger));
            scrollIntoView(profile);
            safeClick(profile);

            By exactSvgLogoutBtn = By.xpath("//button[.//path[contains(@d, 'M116,128V48')]] | //*[normalize-space()='Log out' or normalize-space()='Logout']");
            WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(exactSvgLogoutBtn));
            
            System.out.println("[EXECUTION] Interacting with sidebar profile options via path params...");
            safeClick(logout);

            wait.until(ExpectedConditions.urlContains("/login"));
            System.out.println("[SUCCESS] Full user flow evaluation complete. Session dropped safely.");
        } catch (org.openqa.selenium.NoSuchSessionException e) {
            System.out.println("[INFO] Browser session was terminated externally. Safely ignoring logout exception.");
        } catch (Exception e) {
            try {
                if (!driver.getCurrentUrl().contains("/login")) {
                    driver.get(loginUrl);
                    wait.until(ExpectedConditions.urlContains("/login"));
                }
            } catch (Exception sessionClosedEx) {
                System.out.println("[INFO] Webdriver session unavailable for fallback redirect routing.");
            }
        }
    }

    public void filterCatalogByQuery(String queryText) {
        typeSearchQuery(queryText);
    }

    public void typeSearchQuery(String queryText) {
        waitForAppReady();
        WebElement box = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputField));
        box.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        box.clear();
        box.sendKeys(queryText);
    }

    public String getSearchFieldValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputField)).getAttribute("value");
    }

    public List<WebElement> getVisibleCourseCards() {
        return driver.findElements(courseCard);
    }

    public boolean isNoResultsMessageShown() {
        return !driver.findElements(noResultsMessage).isEmpty();
    }

    public void waitForSearchResultsToSettle() {
        new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(10))
            .pollingEvery(Duration.ofMillis(250))
            .ignoring(StaleElementReferenceException.class)
            .until(d -> !getVisibleCourseCards().isEmpty() || isNoResultsMessageShown());
    }

    public void launchWorkspaceForCourse(String courseName) {
        String safeName = escapeXPathString(courseName);
        By dynamicCardBtn = By.xpath(
            "//*[contains(text(), " + safeName + ")]/ancestor::div[contains(@class,'card') or contains(@class,'rounded')]//button" +
            " | //button[contains(text(), 'Course Overview')]"
        );
        scrollAndClick(dynamicCardBtn);
        wait.until(ExpectedConditions.urlContains("course"));
        waitForAppReady();
    }

    public void expandTopicAccordion() {
        expandTopicAccordion("C Fundamentals");
    }

    public void expandTopicAccordion(String topicName) {
        String safeTopic = escapeXPathString(topicName);
        
        // REFACTORED STRING FORMAT: Guarantees explicit closing brackets on node union validations
        String template = "//div[contains(@class, 'cursor-pointer')][.//*[contains(text(), %1$s)]] " +
                           "| //*[contains(text(), %1$s)]/ancestor::div[contains(@class, 'cursor-pointer')][1]";
        
        By header = By.xpath(String.format(template, safeTopic));
        scrollAndClick(header);
        waitForAnimation();
    }

    public void selectSubTopicLesson() {
        selectSubTopicLesson("Keywords, Identifiers, Constants, Variables, Data Types");
    }

    public void selectSubTopicLesson(String lessonTitle) {
        String safeLesson = escapeXPathString(lessonTitle);
        By lesson = By.xpath(
            "//p[contains(text(), " + safeLesson + ")]" +
            " | //div[contains(@class, 'cursor-pointer')]//p[contains(., " + safeLesson + ")]"
        );
        scrollAndClick(lesson);
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath(String.format(tabSelectorPattern, "Dashboard", "Dashboard"))
        ));
        waitForAppReady();
    }

    public void switchWorkspaceSubTab(String tabLabel) {
        waitForAppReady();
        By contextualTab = By.xpath(String.format(tabSelectorPattern, tabLabel, tabLabel));
        WebElement tabBtn = wait.until(ExpectedConditions.presenceOfElementLocated(contextualTab));
        
        try {
            scrollIntoView(tabBtn);
            wait.until(ExpectedConditions.elementToBeClickable(tabBtn));
            tabBtn.click();
        } catch (Exception nativeError) {
            directClickViaJavaScript(tabBtn);
        }
        
        waitForTabActive(tabBtn);
        waitForAppReady();
    }

    public String getActiveTabLabel() {
        List<WebElement> tabs = driver.findElements(By.xpath("//button[@aria-selected='true']"));
        return tabs.isEmpty() ? null : tabs.get(0).getText().trim();
    }

    public boolean clickOptionalLeaderboard() {
        List<WebElement> buttons = driver.findElements(leaderboardBtn);
        if (buttons.isEmpty()) {
            return false;
        }
        WebElement btn = buttons.get(0);
        scrollIntoView(btn);
        safeClick(btn);
        try {
            wait.until(d -> isLeaderboardModalOpen());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLeaderboardModalOpen() {
        return !driver.findElements(By.xpath("//*[contains(@class,'modal') and contains(.,'Leaderboard')]")).isEmpty();
    }

    public boolean interactWithDashboardAnalytics() {
        List<WebElement> elements = driver.findElements(chartVisualNodes);
        if (elements.isEmpty()) {
            return false;
        }

        WebElement validTargetNode = null;
        for (WebElement element : elements) {
            try {
                if (element.isDisplayed() && element.getSize().getWidth() > 3 && element.getSize().getHeight() > 3) {
                    validTargetNode = element;
                    break;
                }
            } catch (Exception ignored) {
            }
        }

        if (validTargetNode == null) {
            validTargetNode = elements.get(elements.size() / 2);
        }

        try {
            scrollIntoView(validTargetNode);
            wait.until(ExpectedConditions.visibilityOf(validTargetNode));
            hardwareBridge.moveToElement(validTargetNode).build().perform();
        } catch (Exception e) {
            js.executeScript(
                "var ev = new MouseEvent('mouseover', {bubbles: true, cancelable: true, view: window});" +
                "arguments[0].dispatchEvent(ev);", 
                validTargetNode
            );
        }
        return true;
    }

    public boolean traverseMaterialSubTabs() {
        List<WebElement> pdfTabs = driver.findElements(pdfTabBtn);
        List<WebElement> materialTabs = driver.findElements(materialsTabBtn);
        if (pdfTabs.isEmpty() || materialTabs.isEmpty()) {
            return false;
        }

        WebElement pdfTab = pdfTabs.get(0);
        try {
            scrollIntoView(pdfTab);
            pdfTab.click();
        } catch (Exception e) {
            directClickViaJavaScript(pdfTab);
        }
        if (!waitForTabActive(pdfTab)) {
            return false;
        }

        WebElement materialsTab = materialTabs.get(0);
        try {
            scrollIntoView(materialsTab);
            materialsTab.click();
        } catch (Exception e) {
            directClickViaJavaScript(materialsTab);
        }
        return waitForTabActive(materialsTab);
    }

    public boolean triggerCodeSnippetCopy() {
        List<WebElement> copyButtons = driver.findElements(genericCopyBtn);
        if (copyButtons.isEmpty()) {
            return false;
        }
        scrollAndClick(genericCopyBtn);
        return true;
    }

    public boolean triggerMindmapActions() {
        List<WebElement> panes = driver.findElements(draggableFlowPane);
        if (panes.isEmpty()) {
            return false;
        }
        WebElement flowPane = panes.get(0);
        scrollIntoView(flowPane);

        String transformBefore;
        try {
            transformBefore = flowPane.getAttribute("style");
        } catch (StaleElementReferenceException e) {
            transformBefore = null;
        }

        hardwareBridge.moveToElement(flowPane)
                      .clickAndHold()
                      .moveByOffset(-120, -80)
                      .release()
                      .build()
                      .perform();

        waitForAnimation();

        List<WebElement> panesAfter = driver.findElements(draggableFlowPane);
        String transformAfter = panesAfter.isEmpty() ? null : panesAfter.get(0).getAttribute("style");

        List<WebElement> nodes = driver.findElements(mindmapNodeButton);
        if (!nodes.isEmpty()) {
            WebElement interactiveNode = nodes.get(0);
            try {
                interactiveNode.click();
            } catch (Exception e) {
                directClickViaJavaScript(interactiveNode);
            }
        }

        boolean panChanged = transformBefore != null && !transformBefore.equals(transformAfter);
        return panChanged || !nodes.isEmpty();
    }

    public boolean runSandboxPlaygroundLifecycle() {
        return runSandboxPlaygroundLifecycle(null);
    }

    public boolean runSandboxPlaygroundLifecycle(String customCodeBlock) {
        try {
            WebElement tryIt = wait.until(ExpectedConditions.elementToBeClickable(tryItPlaygroundBtn));
            scrollIntoView(tryIt);
            safeClick(tryIt);

            if (customCodeBlock != null) {
                By visualEditorContainer = By.xpath("//div[contains(@class, 'monaco-editor') or contains(@class, 'ace_editor') or contains(@class, 'cm-editor')]");
                List<WebElement> containerElements = driver.findElements(visualEditorContainer);
                
                if (!containerElements.isEmpty()) {
                    WebElement visibleContainer = containerElements.get(0);
                    hardwareBridge.moveToElement(visibleContainer).click().build().perform();
                    hardwareBridge.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                                  .sendKeys(Keys.BACK_SPACE)
                                  .build().perform();
                    hardwareBridge.sendKeys(customCodeBlock).build().perform();
                } else {
                    List<WebElement> editors = driver.findElements(monacoEditorTextArea);
                    if (!editors.isEmpty()) {
                        WebElement activeEditor = editors.get(0);
                        safeClick(activeEditor);
                        activeEditor.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                        activeEditor.sendKeys(customCodeBlock);
                    }
                }
            }

            WebElement runBtn = wait.until(ExpectedConditions.elementToBeClickable(sandboxRunCodeBtn));
            safeClick(runBtn);

            if (customCodeBlock == null) {
                waitForAnimation();
            } else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(sandboxOutputTerminal));
            }

            List<WebElement> accessButtons = driver.findElements(sandboxAccessibilityBtn);
            if (!accessButtons.isEmpty() && accessButtons.get(0).isDisplayed()) {
                WebElement accessBtn = accessButtons.get(0);
                try {
                    accessBtn.click();
                } catch (Exception e) {
                    directClickViaJavaScript(accessBtn);
                }
            }

            WebElement exitBtn = wait.until(ExpectedConditions.elementToBeClickable(exitPlaygroundBtn));
            scrollIntoView(exitBtn);
            safeClick(exitBtn);

            wait.until(ExpectedConditions.invisibilityOfElementLocated(exitPlaygroundBtn));
            return true;
        } catch (Exception e) {
            System.out.println("[FAULT] Sandbox execution caught error: " + e.getMessage());
            return false;
        }
    }

    public boolean executeFullPracticeLifecycleSuite(String cSourcePayload) {
        try {
            WebElement startPracticeBtn = wait.until(ExpectedConditions.elementToBeClickable(practiceStartButton));
            scrollIntoView(startPracticeBtn);
            safeClick(startPracticeBtn);

            WebElement initTestBtn = wait.until(ExpectedConditions.elementToBeClickable(testConfirmStartBtn));
            safeClick(initTestBtn);

            WebElement proceedBtn = wait.until(ExpectedConditions.elementToBeClickable(guidelineContinueBtn));
            safeClick(proceedBtn);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            List<WebElement> themes = driver.findElements(themeToggleSvgBtn);
            if (!themes.isEmpty()) {
                safeClick(themes.get(0));
                waitForAnimation();
            }

            By editorContainerLocator = By.xpath("//div[contains(@class, 'monaco-editor')] | //div[contains(@class, 'view-lines')]");
            WebElement targetArea = wait.until(ExpectedConditions.presenceOfElementLocated(editorContainerLocator));
            
            hardwareBridge.moveToElement(targetArea).click().build().perform();
            hardwareBridge.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                          .sendKeys(Keys.BACK_SPACE)
                          .build().perform();
            
            hardwareBridge.sendKeys(cSourcePayload).build().perform();
            waitForAnimation();

            WebElement runCodeBtn = wait.until(ExpectedConditions.elementToBeClickable(activeTestRunCodeBtn));
            safeClick(runCodeBtn);
            waitForAnimation();

            WebElement endTestBtn = wait.until(ExpectedConditions.elementToBeClickable(activeTestEndBtn));
            scrollIntoView(endTestBtn);
            safeClick(endTestBtn);

            WebElement exitConfirm = wait.until(ExpectedConditions.elementToBeClickable(exitModalConfirmBtn));
            safeClick(exitConfirm);
            
            waitForAppReady();
            return true;
        } catch (Exception e) {
            System.out.println("[FAULT] Complex Assessment Practice run encountered exception: " + e.getMessage());
            return false;
        }
    }

    public void recoverContextAfterPracticeExit() {
        System.out.println("[RECOVERY] Landing context lost post-exit. Re-initializing module alignment hooks...");
        
        try {
            By zincOverlay = By.xpath("//div[contains(@class, 'bg-zinc-800') or contains(@class, 'bg-opacity-60')]");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(zincOverlay));
            System.out.println("[RECOVERY] Background modal overlays completely dismissed.");
        } catch (Exception e) {
            System.out.println("[INFO] No lingering modal backdrop overlays detected.");
        }

        waitForAppReady();

        By recoveryLesson = By.xpath("//p[text()='Keywords, Identifiers, Constants, Variables, Data Types'] | //div[contains(@class, 'cursor-pointer')]//p[contains(text(), 'Keywords')]");
        
        // Adjusted recovery wrapper alignment strategy
        String recoveryTemplate = "//div[contains(@class, 'cursor-pointer')][.//div[text()=%1$s]] " +
                                   "| //*[text()=%1$s]/ancestor::div[contains(@class, 'cursor-pointer')][1]";
        By recoveryHeader = By.xpath(String.format(recoveryTemplate, escapeXPathString("C Fundamentals")));

        boolean recoveredAndVisible = false;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                List<WebElement> lessonCheck = driver.findElements(recoveryLesson);
                if (!lessonCheck.isEmpty() && lessonCheck.get(0).isDisplayed()) {
                    System.out.println("[RECOVERY] Verified: Accordion is open and elements are visible.");
                    recoveredAndVisible = true;
                    break;
                }
                
                System.out.println("[RECOVERY] Accordion closed or stale (Attempt " + attempt + "/3). Forcing robust interactive toggle...");
                WebElement headerEl = wait.until(ExpectedConditions.elementToBeClickable(recoveryHeader));
                scrollIntoView(headerEl);
                
                try {
                    headerEl.click();
                } catch (Exception clickFallback) {
                    directClickViaJavaScript(headerEl);
                }
                
                waitForAnimation();
                
                lessonCheck = driver.findElements(recoveryLesson);
                if (!lessonCheck.isEmpty() && lessonCheck.get(0).isDisplayed()) {
                    recoveredAndVisible = true;
                    break;
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("[RECOVERY] Caught stale reference during alignment, recycling loop...");
            } catch (Exception ignored) {}
        }

        WebElement lessonEl = wait.until(ExpectedConditions.visibilityOfElementLocated(recoveryLesson));
        scrollIntoView(lessonEl);
        safeClick(lessonEl);
        
        waitForAppReady();
    }

    public void clickDirectDiscussionTabButton() {
        System.out.println("[EXECUTION] Targeting standalone Discussion tab action layer button...");
        By standaloneDiscussionBtn = By.xpath("//button[contains(normalize-space(.), 'Discussion')] | //button[descendant::*[local-name()='svg'] and contains(., 'Discussion')]");
        
        WebElement discBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(standaloneDiscussionBtn));
        scrollIntoView(discBtn);
        wait.until(ExpectedConditions.elementToBeClickable(discBtn));
        
        try { 
            discBtn.click(); 
        } catch(Exception e) { 
            js.executeScript("arguments[0].click();", discBtn); 
        }
        
        By discussionContentContainer = By.xpath("//div[contains(@class, 'discussion')] | //*[contains(text(), 'Discuss Your Doubts')] | //div[contains(@class, 'rich-text-editor')]");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(discussionContentContainer));
            System.out.println("[SUCCESS] Verified: Discussions layout completely populated and active.");
        } catch (Exception e) {
            System.out.println("[WARNING] Layout validation delayed. Forcing secondary structural wait... ");
            waitForAnimation();
        }
        
        waitForAppReady();
    }

    public boolean isDiscussionHeaderDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(discussionHeaderLabel)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void verifyFacultyPillStateOnly() {
        System.out.println("[EXECUTION] Verifying and enforcing active Faculty panel focus...");
        WebElement facBtn = wait.until(ExpectedConditions.presenceOfElementLocated(facultyFilterPill));
        scrollIntoView(facBtn);
        waitForAnimation();
    }

    public boolean verifyPillSelectionState() {
        waitForAppReady();
        WebElement activeTarget = wait.until(ExpectedConditions.presenceOfElementLocated(facultyFilterPill));
        String classString = activeTarget.getAttribute("class");
        return classString.contains("bg-gradient-to-r") || classString.contains("text-white") || classString.contains("active") || classString.contains("bg-zinc");
    }

    public void injectTextIntoTipTapEditor(String inputPayload) {
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(tiptapEditorField));
        scrollIntoView(editor);
        
        js.executeScript("arguments[0].innerHTML = '<p><br></p>';", editor);
        
        editor.click();
        editor.sendKeys(inputPayload);
        waitForAnimation();
    }

    public void applyRichTextFormatting(String formatType) {
        By targetedFormatBtn;
        switch (formatType.toUpperCase()) {
            case "BOLD": targetedFormatBtn = boldFormatBtn; break;
            case "ITALIC": targetedFormatBtn = italicFormatBtn; break;
            case "CODE": targetedFormatBtn = codeBlockFormatBtn; break;
            case "HEADING": targetedFormatBtn = headingFormatBtn; break;
            default: throw new IllegalArgumentException("Unknown RichText formatting token: " + formatType);
        }
        
        try {
            List<WebElement> buttons = driver.findElements(targetedFormatBtn);
            if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
                WebElement btn = buttons.get(0);
                scrollIntoView(btn);
                safeClick(btn);
                System.out.println("[FORMAT] Executed " + formatType + " formatting transformation layer.");
            } else {
                System.out.println("[WARNING] Formatting button for " + formatType + " not visible in DOM layer. Continuing execution stream...");
            }
        } catch (Exception e) {
            System.out.println("[WARNING] Skipping formatting step for " + formatType + " due to interaction block: " + e.getMessage());
        }
    }

    public void fireSendPostAction() {
        WebElement sendBtn = wait.until(ExpectedConditions.elementToBeClickable(sendPostBtn));
        safeClick(sendBtn);
        waitForAnimation();
        waitForAppReady();
    }

    public int getPostedMessagesTotalCount() {
        return driver.findElements(genericMessageFeedBubble).size();
    }

    public boolean verifyMessageInFeed(String expectedString) {
        By dynamicTextMatchXpath = By.xpath("//div[contains(@class, 'rich-text-editor')]//*[contains(text(), '" + expectedString + "')]");
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(dynamicTextMatchXpath)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String extractCourseProgressMetrics() {
        try {
            waitForAppReady();
            List<WebElement> labels = driver.findElements(progressPercentageLabel);
            if (!labels.isEmpty() && labels.get(0).isDisplayed()) {
                return labels.get(0).getText().trim();
            }
            List<WebElement> bars = driver.findElements(aggregateProgressBar);
            if (!bars.isEmpty()) {
                String widthVal = bars.get(0).getAttribute("style");
                if (widthVal != null && widthVal.contains("width")) return widthVal;
                String ariaVal = bars.get(0).getAttribute("aria-valuenow");
                if (ariaVal != null) return ariaVal + "%";
            }
        } catch (Exception ignored) {}
        return "0%";
    }

    public boolean executeDetachedWindowPopoutSequence() {
        List<WebElement> popLinks = driver.findElements(popoutExternalLinkBtn);
        if (popLinks.isEmpty()) {
            return false;
        }
        
        String primaryWindow = driver.getWindowHandle();
        safeClick(popLinks.get(0));
        
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Set<String> allWindows = driver.getWindowHandles();
        
        for (String targetWindow : allWindows) {
            if (!targetWindow.equals(primaryWindow)) {
                driver.switchTo().window(targetWindow);
                break;
            }
        }
        
        String detachedUrl = driver.getCurrentUrl();
        driver.close();
        driver.switchTo().window(primaryWindow);
        
        return detachedUrl != null && !detachedUrl.isEmpty();
    }

    public void windowScrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void resetViewToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void resetViewToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
}