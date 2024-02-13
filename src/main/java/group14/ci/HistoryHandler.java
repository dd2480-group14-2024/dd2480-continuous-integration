package group14.ci;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryHandler {
    private String builds;
    private String buildLogPath;

    /**
     * Handles the history
     * 
     * @param buildLogPath the path to the build logs
     * @throws UnsupportedEncodingException if it encounters an unsupported encoding
     * @throws IOException                  if an IO error occurs
     */
    public HistoryHandler(String buildLogPath) throws UnsupportedEncodingException, IOException {
        this.buildLogPath = buildLogPath;

        // Create the directory if it does not exist
        File directory = new File(buildLogPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + buildLogPath);
            } else {
                System.err.println("Error: Unable to create directory");
            }
        }
    }

    /**
     * 
     * @return builds, the string containing the json representation of the build
     *         logs
     * @throws UnsupportedEncodingException if it encounters an unsupported encoding
     * @throws IOException                  if an IO error occurs
     */
    public String builds() throws UnsupportedEncodingException, IOException {
        JSONObject jBuilds = new JSONObject();
        JSONArray jArray = new JSONArray();
        File directory = new File(buildLogPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals("dummy.txt")) {
                    String fileContent = new String(Files.readAllBytes(file.toPath()), "UTF-8");
                    jArray.put(new JSONObject(fileContent));
                }
            }
            jBuilds.put("builds", jArray);
            this.builds = jBuilds.toString();
        } else {
            System.err.println("Error: Directory not found");
        }

        return builds;
    }

    /**
     * Saves the provided build status in JSON format to a text file at the
     * buildPath.
     *
     * @param shaID         the SHA ID
     * @param compileStatus the compilation status
     * @param testStatus    the test status
     * @throws IOException if an IO error occurs
     */
    public void saveBuildInfo(String shaID, boolean compileStatus, boolean testStatus) throws IOException {
        JSONObject buildInfo = new JSONObject();
        buildInfo.put("ShaID", shaID);
        buildInfo.put("CompileStatus", compileStatus);
        buildInfo.put("TestStatus", testStatus);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        buildInfo.put("Timestamp", dateFormat.format(new Date()));

        // Build the filepath
        String fileName = buildLogPath + File.separator + shaID + ".txt";

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(buildInfo.toString());
            System.out.println("Build information saved to: " + fileName);
        }
    }

    /**
     * Returns the specific build log for the given SHA_ID.
     *
     * @param shaID the SHA_ID of the build log to retrieve
     * @return the build log in JSON format, or null if not found
     */
    public String getBuild(String shaID) {
        String filePath = buildLogPath + File.separator + shaID + ".txt";

        try {
            // Read the content of the file
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
            return fileContent;
        } catch (IOException e) {
            // Handle the case where the file is not found or an IO error occurs
            System.err.println("Error reading build log for SHA_ID " + shaID + ": " + e.getMessage());
            return null;
        }
    }
}