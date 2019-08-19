# Welcome

Thank you for checking out my "Bistro!" restaurant and order processing challenge!  There are two main components delivered in this challenge - a server side piece written in Java (with Spring as web service engine) and a client-side web application written in React / Javascript.  Note that the client side is by no means a professionally developed front end - but it's very useful for adhoc testing and a tool that can be used to visualize the workings of the system.

## Server Code Specifics
As mentioned above, the server is written in Java with Spring as the servlet/web server engine.  This is a maven project so please do the following in the top-level folder to download and install all dependencies you'll need:

#### $ mvn dependency:tree

Once dependencies are downloaded, you can do the following to start the server:

#### $ mvn spring-boot:run

This _should_ run the server on port 8000.  You should see the following displayed on the terminal after running this command:

2019-08-19 15:30:36.880  INFO 35306 --- [  restartedMain] com.redtail.csschallenge.CSSApplication  : Starting CSSApplication on ...

com.redtail.csschallenge.CSSApplication  : Started CSSApplication in 4.674 seconds (JVM running for 5.366)
Read 67 items on the menu
The Bistro! is now taking orders!

## Web Frontend Specifics

The frontend is a React UI (Javascript / JSX) that needs to run on port 3000 (if running on the same machine as the server).  This is because the CORS headers specify only this localhost port to accept requests from same host.  This is an 'npm' based project and there's several dependencies that must be downloaded and installed before running.  This is done using the following npm command from the 'frontend' directory included with this package:

#### $ cd frontend
#### $ npm update

Once that's finished, bringing up the UI is easily done via:

#### $ npm start

Note:  I suggest you start the server before bringing up the UI but this is not entirely necessary.  It's just that the UI is a bit ugly in its default, unpopulated state :-)


