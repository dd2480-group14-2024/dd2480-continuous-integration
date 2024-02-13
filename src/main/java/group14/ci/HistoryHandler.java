package group14.ci;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import org.json.JSONArray;
import org.json.JSONObject;


public class HistoryHandler {
    private String builds;

    /**
     * Handles the history
     * @param buildLogPath the path to the build logs
     * @throws UnsupportedEncodingException if it encounters an unsupported encoding
     * @throws IOException if an IO error occurs
     */
    public HistoryHandler(String buildLogPath) throws UnsupportedEncodingException, IOException {
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
    }

    /**
     * 
     * @return builds, the string containing the json representation of the build logs
     */
    public String builds() {
        return builds;
    }
}