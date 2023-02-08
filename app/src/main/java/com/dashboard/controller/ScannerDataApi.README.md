Show more Details = showMoreDetailsById()
-------------------
curl -X 'GET' \
'http://localhost:8080/scanners/<a unified data Id>' \
-H 'accept: application/json'

Example:

curl -X 'GET' \
'http://localhost:8080/scanners/63107b21d419da06efdf0798' \
-H 'accept: application/json'

Get Versions By ProductName And MicroserviceName= getVersionsByProductNameAndMicroserviceName()
----------------
curl -X 'GET' \
'http://localhost:8080/scanners/versions?productName=<product name>&microserviceName=<micriservice name>&scannerName=<scanner name>' \
-H 'accept: application/json'

Example:
curl -X 'GET' \
'http://localhost:8080/scanners/versions?productName=APEX&microserviceName=north-api-sites&scannerName=Allure' \
-H 'accept: application/json'

Get all available scanner names =getScannersNames().
----------------

curl -X 'GET' \
'http://localhost:8080/scanners/scanners-names' \
-H 'accept: application/json'

Get Report = getIdentifierScanner()
----------------
curl -X 'GET' \
'http://localhost:8080/scanners/reports?productName=<product name>&microserviceName=<microservice name>&version=<git version>&scannerName=<scanner name>' \
-H 'accept: application/json'

Example:
curl -X 'GET' \
'http://localhost:8080/scanners/reports?productName=APEX&microserviceName=north-api-sites&version=main&scannerName=Allure' \
-H 'accept: application/json'


Get Product Names = getAllProductsNames()
--------------
curl -X 'GET' \
'http://localhost:8080/scanners/products-names?scannerName=<scanner name>' \
-H 'accept: application/json'

Example:
curl -X 'GET' \
'http://localhost:8080/scanners/products-names?scannerName=Junit' \
-H 'accept: application/json'

Get microservice names =getMicroservicesNames()
---------------
curl -X 'GET' \
'http://localhost:8080/scanners/microservices-names?productName=<product name>&scannerName=<scanner name>' \
-H 'accept: application/json'

Example:
curl -X 'GET' \
'http://localhost:8080/scanners/microservices-names?productName=APEX&scannerName=OpenApi' \
-H 'accept: application/json'

Get data for graphs by filters =getScannerDataByFilter()
-----------
curl -X 'GET' \
'http://localhost:8080/scanners/graphs-data?fromDate=<optional ,date in format:YYYY-MM-DD>&toDate=<optional ,date in format:YYYY-MM-DD>
&scannerType=< scanner Type sported values:   SECURITY ,TEST_REPORT ,CODE_ANALYZER>
&productName=<optional ,product name >
&serviceName=<optional ,microservice name>
&branchName=<optional , git branch> \
-H 'accept: application/json'

Example:
curl -X 'GET' \
'http://localhost:8080/scanners/graphs-data?fromDate=2022-08-01&toDate=2022-09-12&scannerType=SECURITY&productName=APEX' \
-H 'accept: application/json'

