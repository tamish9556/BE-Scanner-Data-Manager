# openApi data driver
#### Data conversion from a scanner file to a unified data object.
#### Using jackson-parsing technology in the objectMapper library.
###  - handleAndParsingData function:
 input:
       MultipartFile
       UnifiedDataScanner - with basic data
The function converts from a file to a Java object and sends to another function that performs aggregation on the data.
###  - AggregationToUnifiedDataOpenApi function
input:
  OpenApiScannerFile -the java object with the scanner data
 UnifiedDataScanner -to fill into it
 output: After filling in the relevant details from the openapi scanner file.
The function performs aggregations on the java object that contains data from the openApi file  scanner
###  AggregateTestsListFromErrorsList function
the function full in listOfTestToPush tests from changeOperation object.
### getOperation function
return operation or oldOperation from changeOperation
### GetTestsList function
push to allError the tests:  NewEndpoints, MissingEndpoints, ChangedOperations, ChangedSchemas, ChangedElements, DeprecatedEndpoints.
###  AddToErrorsList function
add to allError the errores in changedOperationList
### aggregationCustomizedFields function
add customized feilds- unique fields for spesific test.
### aggregationCustomizedFieldsSpecOpenApi function
add customized feilds- unique fields for spesific test in specOpenApi.
### getScannerType function:
The function returns the file type - OPENAPI.
### getScannerGroupType function
return the scanner group type- SECURITY.