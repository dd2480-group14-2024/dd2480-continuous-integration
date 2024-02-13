package group14.ci;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.TestExecutionSummary;


public class TestRun {

    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        // Create a launcher
        Launcher launcher = LauncherFactory.create();

        // Create a listener to gather the test execution summary
        TestExecutionListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        // Define the test classes you want to run
        Class<?>[] testClasses = { ContinuousIntegrationServerTest.class };

        // Create a request to discover and run tests in the specified classes
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(LauncherDiscoveryRequestBuilder.selectors(LauncherDiscoveryRequestBuilder.selectClass(testClasses)))
                .build();

        // Run the tests
        launcher.execute(request);

        // Get the test execution summary
        TestExecutionSummary summary = ((SummaryGeneratingListener) listener).getSummary();
        System.out.println("Test execution summary: " + summary);

        // Optionally, you can check the test results and take further actions
        if (summary.getFailures().isEmpty()) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("Some tests failed.");
        }
    }
}
