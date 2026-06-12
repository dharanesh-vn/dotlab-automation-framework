package com.dotlab.pages;

import java.time.Duration;
import java.util.List;
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

    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginSubmitBtn = By.xpath("//form//button[@type='submit'] | //button[contains(@class, 'button')]");

    private final By searchInputField = By.xpath("//input[@placeholder='Search courses...']");
    private final By courseCard = By.xpath("//div[contains(@class,'course-card')]");
    private final By noResultsMessage = By.xpath("//*[contains(text(),'No courses found') or contains(text(),'No results')]");

    private final By loadingOverlay = By.xpath("//*[contains(@class,'loading') or contains(@class,'spinner') or @aria-busy='true']");

    private final String tabSelectorPattern = "//button[contains(normalize-space(.), '%s')] | //button[descendant-or-self::text()[contains(., '%s')]]";

    private final By leaderboardBtn = By.xpath("//button[contains(normalize-space(.), 'View Leaderboard')]");
    private final By chartVisualNodes = By.xpath("//*[local-name()='svg']//*[local-name()='rect' or local-name()='path' and contains(@class, 'apexcharts')] | //canvas | //div[contains(@class, 'apexcharts-canvas')]//*[local-name()='path']");

    private final By materialsTabBtn = By.xpath("//button[./span[normalize-space(.)='Materials']] | //button[contains(., 'Materials')]");
    private final By pdfTabBtn = By.xpath("//button[./span[normalize-space(.)='PDF']] | //button[contains(., 'PDF')]");

    private final By draggableFlowPane = By.xpath("//div[contains(@class, 'react-flow__pane') or contains(@class, 'react-flow__renderer')]");
    private final By mindmapNodeButton = By.xpath("//div[contains(@class, 'react-flow__node')]//button | //div[contains(@class, 'csm-mindmap-node')]//button");
    private final By genericCopyBtn = By.xpath("//pre//button | //div[contains(@class, 'code')]//*[local-name()='svg' or contains(@class, 'copy')]");

    private final By tryItPlaygroundBtn = By.xpath("//button[text()='Try it'] | //button[contains(@class, 'button-press-feedback') and contains(., 'Try it')]");
    private final By sandboxRunCodeBtn = By.xpath("//button[contains(@class, 'bg-emerald-500') and normalize-space(.)='Run'] | //button[descendant::*[local-name()='svg'] and normalize-space(.)='Run']");
    private final By sandboxAccessibilityBtn = By.xpath("//div[contains(@class, 'sandbox') or contains(@class, 'playground')]//button[contains(@class, 'bg-amber-200')] | //button[@title='Accessibility Assistant']");
    private final By exitPlaygroundBtn = By.xpath("//button[contains(@class, 'bg-orange-600') and normalize-space(.)='Exit'] | //button[descendant::*[local-name()='svg'] and normalize-space(.)='Exit']");

    private final By profileDropdownTrigger = By.xpath("//div[contains(@class,'profile-container')] | //*[contains(@class, 'footer')]//img/ancestor::div[1] | //div[contains(@class, 'sidebar')]//button[last()]");
    private final By cleanLogoutTarget = By.xpath("//*[normalize-space()='Log out' or normalize-space()='Logout'] | //button[contains(., 'Log out') or contains(., 'Logout')] | //span[contains(text(), 'Log out') or contains(text(), 'Logout')]");

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

    private void strictClick(WebElement element) {
        element.click();
    }

    private void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            System.out.println("[INVESTIGATE] Native click failed, used JS fallback: " + e.getMessage());
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
            resetViewToTop();
            waitForAppReady();

            WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(profileDropdownTrigger));
            scrollIntoView(profile);
            safeClick(profile);

            WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(cleanLogoutTarget));
            safeClick(logout);

            wait.until(ExpectedConditions.urlContains("/login"));
        } catch (Exception e) {
            if (!driver.getCurrentUrl().contains("/login")) {
                driver.get(loginUrl);
                wait.until(ExpectedConditions.urlContains("/login"));
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
        By header = By.xpath(
            "//div[contains(@class, 'cursor-pointer')][.//*[contains(text(), " + safeTopic + ")]]" +
            " | //*[contains(text(), " + safeTopic + ")]/ancestor::div[contains(@class, 'cursor-pointer')][1]"
        );
        scrollAndClick(header);
        waitForAnimation();
    }

    public void selectSubTopicLesson() {
        selectSubTopicLesson("Variables");
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
        WebElement copyBtn = copyButtons.get(0);
        scrollAndClick(By.xpath("//pre//button | //div[contains(@class, 'code')]//*[local-name()='svg' or contains(@class, 'copy')]"));
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
        try {
            WebElement tryIt = wait.until(ExpectedConditions.elementToBeClickable(tryItPlaygroundBtn));
            scrollIntoView(tryIt);
            safeClick(tryIt);

            WebElement runBtn = wait.until(ExpectedConditions.elementToBeClickable(sandboxRunCodeBtn));
            safeClick(runBtn);

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
            System.out.println("[FAULT] Sandbox playground lifecycle failed: " + e.getMessage());
            return false;
        }
    }

    public void resetViewToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void resetViewToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }
}