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

Further instructions: [Smallest Java CI Repository](https://github.com/KTH-DD2480/smallest-java-ci)


## Contributions
Jodie Ooi:
Leo Vainio:
Luna Ji Chen:
Teodor Morfeldt Gadler: Wrote function and tests for notifications. Contributed to readme.
William Carl Vitalis Nordwall: