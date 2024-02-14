package group14.ci;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)

public class ContinuousIntegrationServerTest {

    private void copyFolder(Path src, Path dest) throws IOException {
        Files.walk(src)
                .forEach(source -> {
                    Path destination = dest.resolve(src.relativize(source));
                    try {
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Tests the cloneRepository method of the ContinuousIntegrationServer class to
     * ensure it can successfully clone a remote Git repository. The test performs
     * the following steps:
     * 1. Creates a new instance of ContinuousIntegrationServer.
     * 2. Defines a test repository URL and branch name to simulate cloning a real
     * Git repository.
     * 3. Invokes the cloneRepository method with the test repository URL and branch
     * name.
     * 4. Asserts that the method returns a non-null path, indicating that some
     * cloning activity has taken place.
     * 5. Checks for the existence of the ".git" directory within the cloned
     * repository's path to confirm that a valid Git repository has been cloned.
     * This test verifies the ability of the cloneRepository method to interact with
     * Git and clone repositories, as indicated by the presence of
     * repository-specific metadata (the ".git" directory).
     */

    @Test
    void testCloneRepository(@TempDir Path tempDir) {
        ContinuousIntegrationServer server = new ContinuousIntegrationServer();

        String testRepoUrl = "https://github.com/lvainio/dd2480-continuous-integration";
        String testBranch = "refs/heads/main";

        Path clonedRepoPath = server.cloneRepository(testRepoUrl, testBranch);

        assertNotNull(clonedRepoPath, "The cloned repository path should not be null.");
        assertTrue(Files.exists(clonedRepoPath.resolve(".git")),
                "The .git directory should exist, indicating a successful clone.");
    }

    /**
     * Tests the compileProject method of the ContinuousIntegrationServer class by
     * performing the following steps:
     * 1. Creates a new instance of ContinuousIntegrationServer.
     * 2. Copies a sample Maven project from the test resources to a temporary
     * directory.
     * 3. Invokes the compileProject method with the path to the temporary directory
     * containing the copied Maven project.
     * 4. Asserts that the compileProject method returns true, indicating the Maven
     * project was compiled successfully.
     * 5. Verifies the presence of the 'target' directory within the temporary
     * directory, which further indicates a successful Maven build.
     * This test ensures that the compileProject method can correctly compile a
     * Maven project and produce the expected 'target' directory as a result of the
     * compilation.
     */
    @Test
    void testCompileProject(@TempDir Path tempDir) throws Exception {
        ContinuousIntegrationServer server = new ContinuousIntegrationServer();

        Path resourceDirectory = Paths.get("src/test/java/group14/ci/sampleMavenProject");
        copyFolder(resourceDirectory, tempDir);

        // Use the tempDir with the copied Maven project for testing
        boolean compileResult = server.compileProject(tempDir);

        assertTrue(compileResult, "The project should compile successfully.");

        // Check if the 'target' directory exists in the temporary directory after
        // compilation
        Path targetDir = tempDir.resolve("target");
        assertTrue(Files.exists(targetDir) && Files.isDirectory(targetDir),
                "The 'target' directory should exist after successful compilation.");

    }

    @Test
    public void testTestProject(@TempDir Path tempDir) throws Exception {
        ContinuousIntegrationServer server = new ContinuousIntegrationServer();

        Path resourceDirectory = Paths.get("src/test/java/group14/ci/sampleMavenProject");
        copyFolder(resourceDirectory, tempDir);

        // Use the tempDir with the copied Maven project for testing
        boolean compileResult = server.testProject(tempDir);

        assertTrue(compileResult, "The project should compile successfully.");
    }

    /**
     * When notifyGitHubCommitStatus receives an invalid url (such that it does not
     * point to an actual repository) it should throw a FileNotFound exception
     */
    @Test
    void testNotifyBadRepoUrl() {
        ContinuousIntegrationServer c = new ContinuousIntegrationServer();
        assertThrows(FileNotFoundException.class, () -> {
            c.notifyGitHubCommitStatus(
                    "invalidurl",
                    "author",
                    "129412098124",
                    true,
                    true);
        });
    }

    /***
     * notifyGitHubCommitStatus should not throw any exceptions when the arguments
     * are valid and the Http connection return a 201 code (successful connection)
     * 
     * @throws IOException
     */
    @Test
    void testNotifyGitHubCommitStatusSuccess() throws IOException {
        HttpURLConnection mockedConnection = mock(HttpURLConnection.class);
        when(mockedConnection.getResponseCode()).thenReturn(201);
        when(mockedConnection.getOutputStream()).thenReturn(mock(OutputStream.class));

        ContinuousIntegrationServer spyClass = Mockito.spy(new ContinuousIntegrationServer());
        when(spyClass.createConnection("repo", "owner", "129412098124"))
                .thenReturn(mockedConnection);

        assertDoesNotThrow(() -> {
            spyClass.notifyGitHubCommitStatus(
                    "repo",
                    "owner",
                    "129412098124",
                    true,
                    true);
        });
    }
}
