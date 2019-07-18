# birtsamplereport
This project demonstrates the rendering of reports designed using <a href="https://www.eclipse.org/birt/getting-started/" target="">BIRT</a> Design Engine.

It is a servlet application and can be accessed in the web browser using /WebReport.
It supports 2 document types.
1. /WebReport - PDF - Default
2. /WebReport?docType=HTML 

Software Requirements:
1. Any application server. I have tested with Apache Tomcat 8.4.x. 
2. JDK 7 or more
3. MySQL 5.x. I have used driver version mysql-connector-java-5.1.47 and is available under WEB-INF/lib
4. BIRT Report Designer -  to update the datasource

Pre-requisites:

1. Update the datasource configuration in db_sample_report.rptdesign using BIRT Report Designer.
2. Execute the test scripts under WebContent/testscript.
