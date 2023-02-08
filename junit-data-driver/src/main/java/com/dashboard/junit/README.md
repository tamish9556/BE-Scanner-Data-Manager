# Junit scanner data driver

#### Data conversion from a scanner file to a unified data object.
#### Using jaxbContext technology .

### handleAndParsingData function:

    input: 
       MultipartFile - Scanner file
       UnifiedDataScanner - With basic data
The function converts from a file to a Java object and sends to another function that performs aggregation on the data.

### aggregateJunitToUnifedData function:

    input:
      testsuite - The java object with the scanner data
      UnifiedDataScanner - To fill into it
    output:
      UnifiedDataScanner - After filling in the relevant details from the junit scanner file.
The function performs aggregations on the java object that contains data from the file junit scanner


### getScannerType
The function returns the file type - junit

### getScannerGroupType
The function returns the Scanner type - TEST_REPORT
