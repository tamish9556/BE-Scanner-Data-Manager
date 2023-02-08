# allure data driver

#### Data conversion from a scanner file to a unified data object.
#### Using jackson-parsing technology in the objectMapper library.

### handleAndParsingData function:

    input: 
       MultipartFile
       UnifiedDataScanner - with basic data
The function converts from a file to a Java object and sends to another function that performs aggregation on the data.

### AggregationToUnifiedDataAllureScanner function:

    input:
      AllureFile - the java object with the scanner data
      UnifiedDataScanner - to fill into it
    output:
      UnifiedDataScanner - After filling in the relevant details from the allure scanner file.
The function performs aggregations on the java object that contains data from the file allure scanner


### GetTestList

    input:
        allureGlobalDetails - list of details to get only the tests list.
    output:
        List<TestDetails> - lists of test from allureGlobalDetails.
The function extracts the required list of tests from the Java object with the data from Allure Scanner file.

### AggregateTestsList

    input:
        tests - list of TestDetails.
    output:
        List<ScannerTest> - list of tests after aggregations to put into the unified data.
The function performs aggregations on the list of tests and creates a list of tests in accordance with the desired structure of the unified data.

### FillInGlobalCustomizedFields
    input: 
        allure file - the object that contains the Allure scanner file data.
    output:
        Map<String, String> - contains the customized fields after aggregation.
The function takes relevant data that is unique to this file and inserts it into a map that will be inserted into Unified data.

### FillInOneTestCustomizedFields
    input: 
        allure file - the object that contains the Allure scanner file data.
    output:
        Map<String, String> - contains the customized fields after aggregation.
The function takes relevant data that is unique to this file and inserts it into a map that will be inserted into each test in the Unified data.

### getScannerType
The function returns the file type - allure






