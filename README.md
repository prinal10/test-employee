# test-employee

A spring boot app for employee api.

## Setup

--Note-- Skip Java setup if you already have a JDK installed

### Java:

- Open browser to https://adoptopenjdk.net/
- Download OpenJDK 11 (LTS) with HotSpot JVM
- Follow installation instructions if needed https://adoptopenjdk.net/installation.html
- Sub Item 2

### JAR:

- got to the directory where the `pom.xml` file if located
- run ```mvn package``` command which will create a `test-employee-0.0.1-SNAPSHOT` jar in the target directory at the root
- to start the app run ```java -jar test-employee-0.0.1-SNAPSHOT.jar``` command

###Link:

- To access the swagger docs, go to =>  http://localhost:8080/swagger-ui.html
