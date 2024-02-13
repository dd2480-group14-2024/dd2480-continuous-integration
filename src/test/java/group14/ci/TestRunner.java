package group14.ci;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public class TestRunner {

    public static void main(String[] args) {
        runTests();
    }
    public static void runTests() {
        // Create a launcher
        Launcher launcher = LauncherFactory.create();

        // Create a listener to gather the test execution summary
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        // Define the test classes you want to run
        Class<?>[] testClasses = { ContinuousIntegrationServerTest.class };

        // Create a request to discover and run tests in the specified classes
        DiscoverySelector[] selectors = new DiscoverySelector[testClasses.length];
        for (int i = 0; i < testClasses.length; i++) {
            selectors[i] = DiscoverySelectors.selectClass(testClasses[i]);
        }
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectors)
                .build();

        // Run the tests
        launcher.execute(request);

        // Get the test execution summary
        // You can use the summary to check the test results and take further actions
        System.out.println("Test execution summary: " + listener.getSummary());
    }
}
