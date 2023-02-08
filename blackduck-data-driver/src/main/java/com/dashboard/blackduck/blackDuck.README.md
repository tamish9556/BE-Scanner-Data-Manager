# blackDuck data driver

#### Data conversion from a scanner file to a unified data object.
#### Using request api to blackDuck api and parsing  with  objectMapper library.

### handleAndParsingData function:
    input: 
        UnifiedDataScanner - with basic data.
The function converts from a blackDuck response api to a Java object 
and sends to another function that performs aggregation on the data.

### getBlackDuckComponents function:
    input:
        UnifiedDataScanner - to take the service name to the request api.
The function performs request api to get the list ofComponent
and take the specific component of the current service name.

### getComponentsVulnerabilities function:
    input: 
        urlToComponent - url to current compnent.
        UnifiedDataScanner - to fill into it.
The function performs request api to get the list vulnerabilities  of the current component.

### getVulnerabilitiesDetails function:
    input: 
        urlVulnerabilities - url to vulnerability details list.
        UnifiedDataScanner - to fill into it.
        CountSeverity- model to fill in the count of tests and count sevrity.
        tests- list of tests to fill in the of the current vulnerability.
The function performs request api to get the vulnerabilities details list.

### getSeverity function:
    input: 
        severity - the severity of the vulnerability.
    output: 
        severity - the severity of the vulnerability by the enum value.
The function converts the severity from string to enum value.
### fillCustomizedFields function:
    input: TestModel- current test.
The function fills the customized fields
### fillDataFromTests function:
    input: 
        testList- the list of test from  current vulnerability.
        tests- list of tests to fill in from the testList to unified data format.
        CountSeverity- model to fill in the count of tests and count severity.
    output: 
        List<ScannerTest> - the list of tests in the ScannerTest model.
The function performs aggregations on the list of tests and
creates a list of tests in accordance with the desired structure of the unified data.
### aggregateBlackDuckToUnifiedData function:
    input: 
        unifiedDataScanner - the unified data scanner to fill into it.
        testList - the test list of the current vulnerability.
        CountSeverity- model to fill in the count of tests and count severity.
        tests- list of tests to fill in from the testList to unified data format.
The function performs aggregations on the java object that contains data 
from the blackDuck vulnerabilities details.
### fillCountSeverity function:
    input: 
        severity - the severity of the vulnerability.
        CountSeverity- model to fill in the count of tests and count severity.
The function update the count of severityType in accordance to send severity value.
### getScannerType function:
The function returns the file type - blackDuck