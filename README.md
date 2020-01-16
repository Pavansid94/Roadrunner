# Roadrunner
Fact checking Mini-Project to test the correctness of facts (sentences in the form of text).

Step 1: Fact-Checker is a Java application with Maven as the Build tool.It requires Java 8 and Maven to be already setup.if not use the following links to download & install them:
	- https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html (For Java 8)
	- https://maven.apache.org/download.cgi (For Maven)

Step 2:Once the above setup and the Fact-Checker is in place,build the Fact-Checker using command "mvn clean install" or in IDEs as Maven -> clean and Maven -> install.let the project download the required dependencies which will take a bit.

Step 3:After This,Run the application by running the file "FactCheckerApplication.java"(i.e, Run As -> Java Application),this contains main() that bootstraps the application.

Step 4:The "SNLP2019_training" and "SNLP2019_testing" are bundled within the project itself.No need to import again.the turtle files "output_train.ttl" and "output_test.ttl" respectively are the output turtle files generated for the datasets under the directory "src/main/resources".

Step 5:That is it.

P.S,While running,The application might throw exceptions in between.This is just because of the failed HTTP call.Ignore this, as the application will continue to run even after this.The application ends with printing "Execution Completed..." onto the output console.This is when we know that the application has finished its execution.

