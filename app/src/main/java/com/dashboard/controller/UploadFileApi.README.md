curl -X 'POST' \
'http://localhost:8080/scanners/upload-file/<productName>/<scannerName - sported values:Allure,Junit,Prisma,OpenApi,BlackDuck>' \
-H 'accept: */*' \
-H 'Content-Type: multipart/form-data' \
-F 'scannerDataFile=<input scanner data file>'  \
-F 'microserviceName=<microservice name>' \
-F 'branchVersion=<git branch>' \
-F 'testName=<optional ,test name>' \
-F 'buildNumber=<build>'

Examples:

Allure:

curl -X 'POST' \
'http://localhost:8080/scanners/upload-file/APEX/Allure' \
-H 'accept: */*' \
-H 'Content-Type: multipart/form-data' \
-F 'scannerDataFile=@suites.json;type=application/json' \
-F 'microserviceName=north-api-sites' \
-F 'branchVersion=main' \
-F 'testName=testAllure' \
-F 'buildNumber=00_02_02'

Expected input file:
suites.json
Example for input file:
https://github.com/dell-bootcamp/scanners-data/blob/main/Allure/1/allure-report/data/suites.json


Junit:

curl -X 'POST' \
'http://localhost:8080/scanners/upload-file/EDGE/Junit' \
-H 'accept: */*' \
-H 'Content-Type: multipart/form-data' \
-F 'scannerDataFile=@TEST-com.dell.dtc.support.documents.application.SupportDocumentsApplicationTest.xml;type=text/xml' \
-F 'microserviceName=north-api-sites' \
-F 'branchVersion=main' \
-F 'testName=testJunit' \
-F 'buildNumber=00_02_02'

Expected input file:
TEST-com.dell.dtc.support.documents.application.SupportDocumentsApplicationTest.xml
Example for input file:
https://github.com/dell-bootcamp/scanners-data/JUnit/1/surefire-reports/TEST-com.dell.dtc.support.documents.application.SupportDocumentsApplicationTest.xml

OpenApi

curl -X 'POST' \
'http://localhost:8080/scanners/upload-file/APEX/OpeanApi+' \
-H 'accept: */*' \
-H 'Content-Type: multipart/form-data' \
-F 'scannerDataFile=@openApiScanResult.json;type=application/json' \
-F 'microserviceName=north-api-sites' \
-F 'branchVersion=main' \
-F 'testName=testOpenApi' \
-F 'buildNumber=00_07_02'

Expected input file:
openApiScanResult.json
Example for input file:
https://github.com/dell-bootcamp/scanners-data/OpenAPI/1/openApiScanResult.json

prisma

curl -X 'POST' \
'http://localhost:8080/scanners/upload-file/APEX/Prisma' \
-H 'accept: */*' \
-H 'Content-Type: multipart/form-data' \
-F 'scannerDataFile=@prisma.json;type=application/json' \
-F 'microserviceName=north-api-sites' \
-F 'branchVersion=main' \
-F 'testName=testPrisma' \
-F 'buildNumber=00_05_00'

Expected input file:
prisma.json
Example for input file:
https://github.com/dell-bootcamp/scanners-data/Prisma/1/prisma.json

BlackDuck:

curl -X 'POST' \
'http://localhost:8080/scanners/upload-file/EDGE/BlackDuck' \
-H 'accept: */*' \
-H 'Content-Type: multipart/form-data' \
-F 'microserviceName=hzp-event-svc' \
-F 'branchVersion=main' \
-F 'testName=testBlackduck' \
-F 'buildNumber=00_00_08'
