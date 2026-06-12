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
 * Codebase Identity: CourseTestingSuite - Phase 2 Finalized Validation
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
        System.out.println("[EXECUTION] Injecting string sequence parameters into catalog query fields... ");
        courseBoard.filterCatalogByQuery("C Programming");
        Assert.assertEquals(courseBoard.getSearchFieldValue(), "C Programming", "[FAULT] Query entry mismatch.");
        System.out.println("[SUCCESS] Found matching target course cards in layout frame.");
    }

    @Test(priority = 4, dependsOnMethods = {"verifyCourseCatalogFiltering"}, description = "Launches target active workspace view panels.")
    public void verifyOpenTargetActiveCourseWorkspace() {
        System.out.println("[EXECUTION] Triggering workspace card entry action layer... ");
        courseBoard.launchWorkspaceForCourse("C Programming");
        System.out.println("[SUCCESS] Arrived safely within the primary course overview framework.");
    }

    @Test(priority = 5, dependsOnMethods = {"verifyOpenTargetActiveCourseWorkspace"}, description = "Expands parent topic accordion wrappers to reveal nested child assignments.")
    public void verifyTopicAccordionExpansion() throws InterruptedException {
        System.out.println("[EXECUTION] Querying parent module accordion headers...");
        courseBoard.expandTopicAccordion();
        System.out.println("[SUCCESS] Core accordion toggled. Awaiting layout stabilization....");
    }

    @Test(priority = 6, dependsOnMethods = {"verifyTopicAccordionExpansion"}, description = "Selects specialized nested sub-topics to render live sandbox compiler modules and active sub-tabs.")
    public void verifySpecificSubTopicLessonSelection() throws InterruptedException {
        System.out.println("[EXECUTION] Targeting specific sub-topic module item link element...");
        courseBoard.selectSubTopicLesson();
        System.out.println("[SUCCESS] Sub-topic targeted successfully. Confirming load parameters for editor views...");
    }

    @Test(priority = 7, dependsOnMethods = {"verifySpecificSubTopicLessonSelection"}, description = "Traverses sequentially through isolated structural sub-tabs down to the practice layout.")
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

        // Phase 3: Transition and land specifically on the Practice Workspace view panel
        System.out.println("[EXECUTION] Parking workspace on the active Practice sub-layout panel...");
        courseBoard.resetViewToBottom();
        courseBoard.switchWorkspaceSubTab("Practice");
        
        courseBoard.resetViewToTop();
        System.out.println("[SUCCESS] Left sidebar tab validation complete; standing by inside Practice matrix.");
    }

    @Test(priority = 8, dependsOnMethods = {"verifyCourseWorkspaceSubTabsLayout"}, description = "Validates the full interactive practice lifecycle suite matching live image requirements.")
    public void verifyFullPracticeLayoutLifecycle() {
        System.out.println("[EXECUTION] Initiating End-to-End Practice Sandbox Assessment Run...");

        String cCodePayload = "#include <stdio.h>\n\n" +
                              "int main() {\n" +
                              "    printf(\"Hello, World!\\n\");\n" +
                              "    return 0;\n" +
                              "}";

        boolean practiceCompletedCleanly = courseBoard.executeFullPracticeLifecycleSuite(cCodePayload);
        Assert.assertTrue(practiceCompletedCleanly, "[FAULT] The automated practice workspace suite encountered a runtime break.");
        System.out.println("[SUCCESS] Practice compilation assessment module cycled flawlessly.");
    }

    @Test(priority = 9, dependsOnMethods = {"verifyFullPracticeLayoutLifecycle"}, description = "Executes complete Phase 2 comprehensive testing loop inside the Live Discussion board interface.")
    public void verifyPostPracticeDiscussionNavigationAndLogout() {
        System.out.println("[EXECUTION] Navigating to the Discussions workspace layout section via recovery pipelines...");
        
        // Step A: Recover layout state from the course homepage landing frame
        courseBoard.recoverContextAfterPracticeExit();
        
        // Step B: Target and click the standalone Discussion tab button element
        courseBoard.clickDirectDiscussionTabButton();

        // Checkpoint 1: Assert that the layout has actually rendered the discussion view framework
        Assert.assertTrue(courseBoard.isDiscussionHeaderDisplayed(), 
            "[FAULT] Automation failed to visually render the live discussion board interface layout.");
        System.out.println("[SUCCESS] Confirmed driver focus is parked inside active Discussion panel components.");

        // Checkpoint 2: Validate Faculty mode filter focus is explicitly verified
        courseBoard.verifyFacultyPillStateOnly();
        Assert.assertTrue(courseBoard.verifyPillSelectionState(), 
            "[FAULT] Discussion board did not maintain safe focus on the default Faculty layout configuration.");
        System.out.println("[SUCCESS] Confirmed default active state matches 'Faculty' layout configuration safely.");

        // Step C: Type automated input payload into the TipTap Editor field directly inside Faculty view
        String payloadText = "Automated Syntax Validation Sequence. Testing Rich Text Action Layers.";
        System.out.println("[EXECUTION] Transmitting character sequence streams into TipTap framework workspace...");
        courseBoard.injectTextIntoTipTapEditor(payloadText);

        // Step D: Validate and exhaustively cycle through formatting toolbar layers
        System.out.println("[EXECUTION] Verifying Rich Text Interactive Formatting Controls...");
        courseBoard.applyRichTextFormatting("BOLD");
        courseBoard.applyRichTextFormatting("ITALIC");
        courseBoard.applyRichTextFormatting("CODE");
        courseBoard.applyRichTextFormatting("HEADING");
        System.out.println("[SUCCESS] Formatting toolbar simulation complete. Controls interactive and firing.");

        // Step E: Transmit message and await platform data sync lifecycle
        System.out.println("[EXECUTION] Dispatching message payload string out to active pipeline rails... ");
        courseBoard.fireSendPostAction();
        
        System.out.println("[EXECUTION] Holding thread operational context for 3000ms for database stabilization synchronization...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[SUCCESS] Hold complete. Post-submission processing cycle completed.");

        // Step F: Final secure session destruction handshake
        System.out.println("[EXECUTION] Interacting with sidebar profile options for termination context...");
        courseBoard.terminateSessionSecurely();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "[FAULT] Destination mismatch. Failed to arrive back at login endpoint.");
        System.out.println("[SUCCESS] Full user flow evaluation complete. Session dropped safely.");
    }

    @AfterClass(alwaysRun = true)
    public void tearDownSuiteContext() {
        if (driver != null) {
            driver.quit();
            System.out.println("[INFO] Standalone Course testing run complete. Dropping webdriver context framework safely.");
        }
    }
}