# DD2480 Continuous Integration
This repo contains a basic continuous integration (CI) server made for the course DD2480 at KTH. The server supports compiling and testing Java Maven projects. It can also update the commit status of your repo.


## Setup 
1. Make sure that you have Java, Maven and ngrok installed.
2. Generate a GitHub access token with read/write privlegies for commit statuses for the repo you want to use the server against. Add this token to the string ```GITHUB_TOKEN``` in ContinuousIntegrationServer.java.  
3. Compile and run the program by the commands:
   ```mvn clean install```
   ```mvn exec:java```
4. Start ngrok for port 8080 with:
   ```ngrok http 8080```
5. Paste the URL provided by ngrok into the webhook section of your repo. Make sure to select the JSON format.

Tests can be executed through the ```mvn test``` command.

Javadoc can be generated with mvn javadoc:javadoc.

Further instructions: [Smallest Java CI Repository](https://github.com/KTH-DD2480/smallest-java-ci)


## Details of implementations and unit tests

### Compilation
Github webhooks sends a POST request to our server when someone has made a commit to the remote repository. The server then clones the branch that has been commited to into a temporary folder. The ```compileProject``` function then runs the ```mvn compile``` command in the temporary folder. If no exceptions are thrown and if the exit code of the process is 0, compilation is deemed successful, otherwise not. Testing that the compilation works was done by creating a small mock maven project and running the function on it. We checked that the function returned true, signalling a success, and also that the target folder that maven generates exist after compilation.

### Testing
Triggering Tests: Testing is initiated via GitHub webhooks. Pushing changes to the "assessment" branch in the repository prompts the CI server to automatically execute associated tests. Upon webhook receipt, the CI server extracts repository details, clones, compiles, and runs tests specific to the committed branch. This process ensures changes do not compromise the projects integrity. Testing was done by creating assessment branch and pushed changes for testing, checking CI server log.

### Notifications
When a webhook request has passed through the compilation and testing the step the results are passed to the ```notifyGitHubCommitStatus```. This function uses the GitHub API to set the commit status of the commit that triggered the webhook according to the results of compilation and testing. Authentication is done through pasting your token into the file ```ContinuousIntegrationServer.java```.


## Contributions
Jodie Ooi: Automated testing for ContinuousIntegrationServerTests triggered as webhook. Contributed to readme on testing documentation.

Leo Vainio:
- Wrote the cloneProject function
- Wrote the compileProject function
- Wrote the testProject function
- Set up the repository, organisation and dependencies for the project

Luna Ji Chen:

Teodor Morfeldt Gadler: 
- Wrote function and tests for notifications.
- Contributed to readme/essence analysis
- Wrote tests and parts of the functionality for saving build logs

William Carl Vitalis Nordwall:
- Wrote function for maintaining history of builds


## Essence Standard

| **Seeded**                                                                        |
| --------------------------------------------------------------------------------- |
| [X] The team mission has been defined in terms of the opportunities and outcomes. |
| [X] Constraints on the team's operation are known.                                |
| [X] Mechanisms to grow the team are in place.                                     |
| [X] The composition of the team is defined.                                       |
| [X] Any constraints on where and how the work is carried out are defined.         |
| [X] The team's responsibilities are outlined.                                     |
| [X] The level of team commitment is clear.                                        |
| [X] Required competencies are identified.                                         |
| [X] The team size is determined.                                                  |
| [X] Governance rules are defined.                                                 |
| [X] Leadership model is determined.                                               |

| **Formed**                                                                                         |
| -------------------------------------------------------------------------------------------------- |
| [X] Individual responsibilities are understood.                                                    |
| [X] Enough team members have been recruited to enable the work to progress.                        |
| [X] Every team member understands how the team is organized and what their individual role is.     |
| [X] All team members understand how to perform their work.                                         |
| [X] The team members have met (perhaps virtually) and are beginning to get to know each other.     |
| [X] The team members understand their responsibilities and how they align with their competencies. |
| [X] Team members are accepting work.                                                               |
| [X] Any external collaborators (organizations, teams and individuals) are identified.              |
| [X] Team communication mechanisms have been defined.                                               |
| [X] Each team member commits to working on the team as defined.                                    |

| **Collaborating**                                      |
| ------------------------------------------------------ |
| [-] The team is working as one cohesive unit.          |
| [X] Communication within the team is open and honest.  |
| [X] The team is focused on achieving the team mission. |
| [X] The team members know and trust each other.        |

| **Performing**                                                                                |
| --------------------------------------------------------------------------------------------- |
| [X] The team consistently meets its commitments.                                              |
| [X] The team continuously adapts to the changing context.                                     |
| [X] The team identifies and addresses problems without outside help.                          |
| [X] Effective progress is being achieved with minimal avoidable backtracking and reworking.   |
| [X] Wasted work and the potential for wasted work are continuously identified and eliminated. |


| **Adjourned**                                                             |
| ------------------------------------------------------------------------- |
| [-] The team responsibilities have been handed over or fulfilled.         |
| [-] The team members are available for assignment to other teams.         |
| [-] No further effort is being put in by the team to complete the mission |

The team is somewhere around the performing stage. We have established a model that works well for us and have let us achieve all parts of the performing stage to some degree. However, some criterias, even from earlier levels, could be more well defined. For example we have not defined a leadership model, but so far we make due with ad hoc democratic decisions.
