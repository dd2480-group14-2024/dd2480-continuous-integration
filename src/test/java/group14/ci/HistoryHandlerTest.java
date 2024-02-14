package group14.ci;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.NullPointerException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HistoryHandlerTest {
    private String testPath = "src/test/java/group14/ci/buildLogsTestFolder";

    @BeforeEach
    void setUp() {
        File testPathDirectory = new File(testPath);
        if (testPathDirectory.exists()) {
            deleteDirectory(testPathDirectory);
        }
    }

    /**
     * Removes a directory and all of it contents.
     * 
     * @param directory the directory to delte
     */
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    /**
     * Historhandler throws NullPointerException when buildLogPath is set to null
     */
    @Test
    void invalidFilePathHistoryHandlerTest() {
        assertThrows(NullPointerException.class, () -> new HistoryHandler(null));
    }

    /**
     * History handler creates a directory at buildPath without throwing errors
     */
    @Test
    void validFilePathHistoryHandlerTest() {
        final HistoryHandler[] h = new HistoryHandler[1];

        assertDoesNotThrow(() -> {
            h[0] = new HistoryHandler(testPath);
        });

        assertTrue(new File(testPath).isDirectory());
    }

    /**
     * saveBuildInfo saves the desired json at the desired path
     * 
     * @throws UnsupportedEncodingException from history handler initializaiton
     * @throws IOException                  from history handler initializaiton
     */
    @Test
    void saveBuildInfoSuccessTest() throws UnsupportedEncodingException, IOException {
        HistoryHandler h = new HistoryHandler(testPath);

        h.saveBuildInfo("123", false, false);

        assertTrue(new File(testPath + "/123.txt").exists());

        String fileContent = Files.readString(Paths.get(testPath + "/123.txt"));

        // Expected JSON string
        String expectedJson = "\\{\"TestStatus\":false,\"ShaID\":\"123\",\"Timestamp\":\"[^\"]+\",\"CompileStatus\":false}";

        assertTrue(fileContent.matches(expectedJson));
    }

    /**
     * builds() return all builds created so far
     * 
     * @throws UnsupportedEncodingException from history handler initializaiton
     * @throws IOException                  from history handler initializaiton
     */
    @Test
    void buildsReturnAllBuildsTest() throws UnsupportedEncodingException, IOException {
        HistoryHandler h = new HistoryHandler(testPath);

        h.saveBuildInfo("1", false, true);
        h.saveBuildInfo("2", true, false);

        String expectedJson = "\\{\"builds\":\\[\\{\"TestStatus\":(?:true|false),\"ShaID\":\"[^\"]+\",\"Timestamp\":\"[^\"]+\",\"CompileStatus\":(?:true|false)\\},\\{\"TestStatus\":(?:true|false),\"ShaID\":\"[^\"]+\",\"Timestamp\":\"[^\"]+\",\"CompileStatus\":(?:true|false)\\}\\]\\}";

        assertTrue(h.builds().matches(expectedJson));
    }

    /**
     * getBuild(shaID) returns the build with the shaID
     * 
     * @throws UnsupportedEncodingException from history handler initializaiton
     * @throws IOException                  from history handler initializaiton
     */
    void getBuildReturnsDesiredLogTest() throws IOException {
        HistoryHandler h = new HistoryHandler(testPath);

        h.saveBuildInfo("1", false, true);
        h.saveBuildInfo("2", true, false);

        String expectedJson = "\\{\"TestStatus\":true,\"ShaID\":\"1\",\"Timestamp\":\"[^\"]+\",\"CompileStatus\":false\\}";
        assertTrue(h.getBuild("1").matches(expectedJson));
    }

}
