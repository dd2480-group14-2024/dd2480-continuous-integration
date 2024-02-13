package group14.ci;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
class ContinuousIntegrationServerMainTest {

    /**
     * This test ensures that the main logic of your ContinuousIntegrationServer handles webhooks correctly.
     * You may want to replace the placeholder values in the test payload with actual values from your project.
     */
    @Test
    void testHandleWebhook() {
        try {
            // Create a new ByteArrayOutputStream to capture the output
            ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStreamCaptor));
            ContinuousIntegrationServer ciServer = new ContinuousIntegrationServer();

            // payload data for the project
            String testPayload = "{ \"repository\": { \"clone_url\": \"https://github.com/dd2480-group14-2024/dd2480-continuous-integration\" }, \"ref\": \"refs/heads/assessment\", \"head_commit\": { \"id\": \"abcd1234\" } }";

            // Mock HttpServletRequest and HttpServletResponse
            HttpServletRequest mockRequest = mock(HttpServletRequest.class);
            HttpServletResponse mockResponse = mock(HttpServletResponse.class);

            // Set up the input stream for the mock request
            ByteArrayInputStream inputStream = new ByteArrayInputStream(testPayload.getBytes(StandardCharsets.UTF_8));
            when(mockRequest.getInputStream()).thenReturn(new TestServletInputStream(inputStream));

            // Capture the arguments sent to notifyGitHubCommitStatus method
            ArgumentCaptor<String> repoUrlCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<String> commitIdCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Boolean> compileStatusCaptor = ArgumentCaptor.forClass(Boolean.class);
            ArgumentCaptor<Boolean> testStatusCaptor = ArgumentCaptor.forClass(Boolean.class);

            // Simulate handling a webhook
            ciServer.handle("/webhook", null, mockRequest, mockResponse);

            // handle method output match
            System.setOut(System.out);
            assertTrue(outputStreamCaptor.toString().contains("CI job done"), "Webhook handling should result in a successful test");
            
            // Verify that notifyGitHubCommitStatus is called with the expected arguments
            verify(ciServer, times(1)).notifyGitHubCommitStatus(
                    repoUrlCaptor.capture(),
                    ownerCaptor.capture(),
                    commitIdCaptor.capture(),
                    compileStatusCaptor.capture(),
                    testStatusCaptor.capture());

            // Retrieve the captured values
            String capturedRepoUrl = repoUrlCaptor.getValue();
            String capturedOwner = ownerCaptor.getValue();
            String capturedCommitId = commitIdCaptor.getValue();
            Boolean capturedCompileStatus = compileStatusCaptor.getValue();
            Boolean capturedTestStatus = testStatusCaptor.getValue();

            // Perform additional assertions based on the captured values
            // You may need to replace these with actual values based on your expectations
            assertTrue(capturedRepoUrl.equals("https://github.com/dd2480-group14-2024/dd2480-continuous-integration.git"), "Check capturedRepoUrl");
            assertTrue(capturedOwner.equals("contributor"), "Check capturedOwner");
            assertTrue(capturedCommitId.equals("abcd1234"), "Check capturedCommitId");
            assertTrue(capturedCompileStatus, "Check capturedCompileStatus");
            assertTrue(capturedTestStatus, "Check capturedTestStatus");
        } catch  (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * A simple implementation of ServletInputStream for testing purposes.
     */
    private static class TestServletInputStream extends javax.servlet.ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public TestServletInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() {
            return inputStream.read();
        }
    }
}