package com.dotlab.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

    private static final Duration ANIMATION_BRIDGE = Duration.ofMillis(400);

    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By loginSubmitBtn = By.xpath("//form//button[@type='submit'] | //button[contains(@class, 'button')]");

    private final By searchInputField = By.xpath("//input[@placeholder='Search courses...']");
    private final By courseCard = By.xpath("//div[contains(@class,'course-card')]");
    private final By noResultsMessage = By.xpath("//*[contains(text(),'No courses found') or contains(text(),'No results')]");

    private final String tabSelectorPattern = "//button[contains(normalize-space(.), '%s')] | //button[descendant-or-self::text()[contains(., '%s')]]";

    private final By leaderboardBtn = By.xpath("//button[contains(normalize-space(.), 'View Leaderboard')]");
    private final By chartVisualNodes = By.xpath("//*[local-name()='svg']//*[local-name()='rect' or local-name()='path' and contains(@class, 'apexcharts')] | //canvas");

    private final By materialsTabBtn = By.xpath("//button[./span[normalize-space(.)='Materials']] | //button[contains(., 'Materials')]");
    private final By pdfTabBtn = By.xpath("//button[./span[normalize-space(.)='PDF']] | //button[contains(., 'PDF')]");

    private final By draggableFlowPane = By.xpath("//div[contains(@class, 'react-flow__pane') or contains(@class, 'react-flow__renderer')]");
    private final By mindmapNodeButton = By.xpath("//div[contains(@class, 'react-flow__node')]//button | //div[contains(@class, 'csm-mindmap-node')]//button");
    private final By genericCopyBtn = By.xpath("//pre//button | //div[contains(@class, 'code')]//*[local-name()='svg' or contains(@class, 'copy')]");

    private final By tryItPlaygroundBtn = By.xpath("//button[text()='Try it'] | //button[contains(@class, 'button-press-feedback') and contains(., 'Try it')]");
    private final By sandboxRunCodeBtn = By.xpath("//button[contains(@class, 'bg-emerald-500') and normalize-space(.)='Run'] | //button[descendant::*[local-name()='svg'] and normalize-space(.)='Run']");
    private final By sandboxAccessibilityBtn = By.xpath("//*[local-name()='svg' and contains(@class, 'text-yellow-500')]/ancestor::button[1] | //button[contains(@class, 'bg-amber-200')]");
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

    private void safeClick(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }

    private void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    private void scrollAndClick(By locator) {
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
    }

    public void directRouteToCourseBoard(String targetUrl) {
        driver.get(targetUrl);
        wait.until(ExpectedConditions.urlContains("course-board"));
    }

    public void terminateSessionSecurely() {
        try {
            resetViewToTop();

            WebElement profile = wait.until(ExpectedConditions.elementToBeClickable(profileDropdownTrigger));
            scrollIntoView(profile);
            safeClick(profile);

            try {
                WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(cleanLogoutTarget));
                safeClick(logout);
            } catch (Exception e) {
                if (!driver.getCurrentUrl().contains("/login")) {
                    driver.get(loginUrl);
                }
            }

            wait.until(ExpectedConditions.urlContains("/login"));
        } catch (Exception e) {
            throw new RuntimeException("Secure session termination failed.", e);
        }
    }

    public void filterCatalogByQuery(String queryText) {
        typeSearchQuery(queryText);
    }

    public void typeSearchQuery(String queryText) {
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
    }

    public void switchWorkspaceSubTab(String tabLabel) {
        By contextualTab = By.xpath(String.format(tabSelectorPattern, tabLabel, tabLabel));
        WebElement tabBtn = wait.until(ExpectedConditions.elementToBeClickable(contextualTab));
        scrollIntoView(tabBtn);
        safeClick(tabBtn);

        String ariaSelected = tabBtn.getAttribute("aria-selected");
        if (ariaSelected != null) {
            try {
                wait.until(ExpectedConditions.attributeContains(tabBtn, "aria-selected", "true"));
                return;
            } catch (Exception ignored) {
            }
        }
        waitForAnimation();
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
        return true;
    }

    public boolean isLeaderboardModalOpen() {
        return !driver.findElements(By.xpath("//*[contains(@class,'modal') and contains(.,'Leaderboard')]")).isEmpty();
    }

    public boolean interactWithDashboardAnalytics() {
        List<WebElement> elements = driver.findElements(chartVisualNodes);
        if (elements.isEmpty()) {
            return false;
        }
        WebElement targetNode = elements.get(elements.size() / 2);
        scrollIntoView(targetNode);
        hardwareBridge.moveToElement(targetNode).build().perform();
        return true;
    }

    public boolean traverseMaterialSubTabs() {
        List<WebElement> pdfTabs = driver.findElements(pdfTabBtn);
        List<WebElement> materialTabs = driver.findElements(materialsTabBtn);
        if (pdfTabs.isEmpty() || materialTabs.isEmpty()) {
            return false;
        }

        scrollIntoView(pdfTabs.get(0));
        safeClick(pdfTabs.get(0));
        waitForAnimation();

        scrollIntoView(materialTabs.get(0));
        safeClick(materialTabs.get(0));
        waitForAnimation();
        
        return true;
    }

    public boolean triggerCodeSnippetCopy() {
        List<WebElement> copyButtons = driver.findElements(genericCopyBtn);
        if (copyButtons.isEmpty()) {
            return false;
        }
        WebElement copyBtn = copyButtons.get(0);
        scrollIntoView(copyBtn);
        safeClick(copyBtn);
        return true;
    }

    public boolean triggerMindmapActions() {
        List<WebElement> panes = driver.findElements(draggableFlowPane);
        if (panes.isEmpty()) {
            return false;
        }
        WebElement flowPane = panes.get(0);
        scrollIntoView(flowPane);

        hardwareBridge.moveToElement(flowPane)
                      .clickAndHold()
                      .moveByOffset(-120, -80)
                      .release()
                      .build()
                      .perform();

        waitForAnimation();

        List<WebElement> nodes = driver.findElements(mindmapNodeButton);
        if (!nodes.isEmpty()) {
            safeClick(nodes.get(0));
        }
        return true;
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
                safeClick(accessButtons.get(0));
            }

            WebElement exitBtn = wait.until(ExpectedConditions.elementToBeClickable(exitPlaygroundBtn));
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