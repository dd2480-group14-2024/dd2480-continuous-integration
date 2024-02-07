package group14.ci;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ContinuousIntegrationServer extends AbstractHandler {

    private static final int PORT = 8080;



    /**
     * The main entry point for the Continuous Integration Server application. This
     * method initializes and starts a Jetty server with a ContinuousIntegrationServer
     * handler. 
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args)  {
        Server server = new Server(PORT);
        server.setHandler(new ContinuousIntegrationServer());
        try {
            server.start();
            System.out.println("Server started successfully. Listening on port 8080.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }



    /**
     * this gets run every time github sends a request to our server. I.E. when
     * someone has made a commit to the remote repository. 
     */
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) {

        System.out.println("target: " + target);

        try {
            JSONObject payload = new JSONObject(new JSONTokener(request.getInputStream()));
            String repoUrl = payload.getJSONObject("repository").getString("clone_url");
            String branch = payload.getString("ref");
            System.out.println(repoUrl + " " + branch);
            Path repoPath = cloneRepository(repoUrl, branch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        compileProject();
        runTests();
        
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        try {
            response.getWriter().println("CI job done");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



    /**
     * cloneRepository clones a specific branch of a repository into a 
     * temporary directory and returns the path to the temporary directory.
     * 
     * @param repoUrl The URL to the repository on github.
     *      Example: https://github.com/lvainio/dd2480-continuous-integration.git
     * @param branch The branch to clone.
     *      Example: refs/heads/feat/issue-1/add-some-functionality
     * @return The path to the temporary directory where the repository 
     * has been cloned to if the operation is successful or null if the operation failed. 
     */
    private Path cloneRepository(String repoUrl, String branch) {
        try {
            Path repoPath = Files.createTempDirectory("ci-repo-");
            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(repoPath.toFile())
                    .setBranchesToClone( Arrays.asList( branch ) )
                    .setBranch( branch )
                    .call();
            return repoPath;
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return null;
    }



    /**
     * TODO
     */
    private boolean compileProject() {
        return false;
    }



    /**
     * TODO
     */
    private boolean runTests() {
        return false;
    }
}