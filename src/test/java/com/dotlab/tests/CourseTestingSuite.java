package com.dotlab.tests;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.dotlab.pages.CourseBoardPage;

/**
 * PRODUCTION SUITE: STANDARDIZED PAGE OBJECT DESIGN LIFECYCLE
 * Codebase Identity: CourseTestingSuite
 */
public class CourseTestingSuite {

    private static final String LOGIN_ENDPOINT = "https://dotlab.amypo.ai/login";
    private static final String EXPECTED_COURSE_BOARD_URL = "https://dotlab.amypo.ai/course-board";
    private static final String AUTH_USER = "dharaneshnatarajan@gmail.com";
    private static final String AUTH_PASS = "ZoroLvn!000";

    private WebDriver driver;
    private CourseBoardPage courseBoard;

    @BeforeClass
    public void setupSuiteEnvironment() {
        System.out.println("[INFO] Booting POM Refactored DotLab Testing Suite Engine...");
        ChromeOptions driverOptions = new ChromeOptions();
        driverOptions.addArguments("--remote-allow-origins=*");
        driverOptions.addArguments("--disable-gpu");
        driverOptions.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(driverOptions);
        driver.manage().window().maximize();
        
        WebDriverWait executionWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        courseBoard = new CourseBoardPage(driver, executionWait);
    }

    @Test(priority = 1, description = "Authenticates user credentials against the secure login endpoint.")
    public void verifySecureLoginHandshake() {
        System.out.println("[EXECUTION] Initializing session handshake procedures...");
        courseBoard.executeLoginHandshake(LOGIN_ENDPOINT, AUTH_USER, AUTH_PASS);
        System.out.println("[SUCCESS] Session established for course testing parameters.");
    }

    @Test(priority = 2, dependsOnMethods = {"verifySecureLoginHandshake"}, description = "Validates routing mechanics to the primary catalog layout screen.")
    public void verifyNavigationToCourseBoard() {
        System.out.println("[EXECUTION] Navigating directly to Course Catalog Dashboard...");
        courseBoard.directRouteToCourseBoard(EXPECTED_COURSE_BOARD_URL);
        Assert.assertTrue(driver.getCurrentUrl().contains("course-board"), "[FAULT] Navigation failed to reach course board layout!");
        System.out.println("[SUCCESS] Course catalog dashboard layout populated securely.");
    }

    @Test(priority = 3, dependsOnMethods = {"verifyNavigationToCourseBoard"}, description = "Verifies input parsing matches and filters card assets properly.")
    public void verifyCourseCatalogFiltering() throws InterruptedException {
        System.out.println("[EXECUTION] Injecting string sequence parameters into catalog query fields...");
        courseBoard.filterCatalogByQuery("C Programming");
        Assert.assertEquals(courseBoard.getSearchFieldValue(), "C Programming", "[FAULT] Query entry mismatch.");
        System.out.println("[SUCCESS] Found matching target course cards in layout frame.");
    }

    @Test(priority = 4, dependsOnMethods = {"verifyCourseCatalogFiltering"}, description = "Launches target active workspace view panels.")
    public void verifyOpenTargetActiveCourseWorkspace() {
        System.out.println("[EXECUTION] Triggering workspace card entry action layer...");
        courseBoard.launchWorkspaceForCourse("C Programming");
        System.out.println("[SUCCESS] Arrived safely within the primary course overview framework.");
    }

    @Test(priority = 5, dependsOnMethods = {"verifyOpenTargetActiveCourseWorkspace"}, description = "Expands parent topic accordion wrappers to reveal nested child assignments.")
    public void verifyTopicAccordionExpansion() throws InterruptedException {
        System.out.println("[EXECUTION] Querying parent module accordion headers...");
        courseBoard.expandTopicAccordion();
        System.out.println("[SUCCESS] Core accordion toggled. Awaiting layout stabilization...");
    }

    @Test(priority = 6, dependsOnMethods = {"verifyTopicAccordionExpansion"}, description = "Selects specialized nested sub-topics to render live sandbox compiler modules and active sub-tabs.")
    public void verifySpecificSubTopicLessonSelection() throws InterruptedException {
        System.out.println("[EXECUTION] Targeting specific sub-topic module item link element...");
        courseBoard.selectSubTopicLesson();
        System.out.println("[SUCCESS] Sub-topic targeted successfully. Confirming load parameters for editor views...");
    }

    @Test(priority = 7, dependsOnMethods = {"verifySpecificSubTopicLessonSelection"}, description = "Traverses sequentially through isolated structural sub-tabs.")
    public void verifyCourseWorkspaceSubTabsLayout() throws InterruptedException {
        System.out.println("[EXECUTION] Initiating interface validation sequence across course workspace panel frames...");
        
        // Phase 1: Dashboard Analytics Verification
        courseBoard.switchWorkspaceSubTab("Dashboard");
        courseBoard.interactWithDashboardAnalytics();
        courseBoard.clickOptionalLeaderboard();

        // Phase 2: Material Preparation & Sandbox Engagement
        courseBoard.switchWorkspaceSubTab("Prepare");
        courseBoard.traverseMaterialSubTabs();
        courseBoard.triggerMindmapActions();
        courseBoard.triggerCodeSnippetCopy();
        courseBoard.runSandboxPlaygroundLifecycle();

        // Phase 3: Downstream Carousel Verification Steps
        courseBoard.resetViewToBottom();
        courseBoard.switchWorkspaceSubTab("Practice");
        courseBoard.switchWorkspaceSubTab("Discussion");
        
        courseBoard.resetViewToTop();
        System.out.println("[SUCCESS] Full Course Workspace sub-tab validation executed completely.");
    }

    @Test(priority = 8, dependsOnMethods = {"verifyCourseWorkspaceSubTabsLayout"}, description = "Interacts with sidebar footer configuration slots to execute safe logout logs.")
    public void verifySecureSessionTermination() {
        System.out.println("[EXECUTION] Interacting with sidebar profile options for termination context...");
        courseBoard.terminateSessionSecurely();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "[FAULT] Destination mismatch. Failed to arrive back at login endpoint.");
        System.out.println("[SUCCESS] Session dropped safely. Identity gate redirection completed successfully.");
    }

    @AfterClass(alwaysRun = true)
    public void tearDownSuiteContext() {
        if (driver != null) {
            driver.quit();
            System.out.println("[INFO] Standalone Course testing run complete. Dropping webdriver context framework safely.");
        }
    }
}