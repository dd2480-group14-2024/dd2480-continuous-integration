package group14.ci;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

        System.out.println(target);

        cloneRepository();
        compileProject();
        runTests();

        // TODO: send something back to github i guess.
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
     * TODO
     */
    private boolean cloneRepository() {
        return false;
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