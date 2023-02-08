# Prisma scanner data driver

#### Data conversion from a scanner file to a unified data object.
#### Using jackson-parsing technology in the objectMapper library.

### handleAndParsingData function:

    input: 
       MultipartFile - Scanner file
       UnifiedDataScanner - With basic data
The function converts from a file to a Java object and sends to another function that performs aggregation on the data.

### aggregateTestsList

    input:
        List<Report> - List of vulnerabilities.
    output:
        List<ScannerTest> - List of tests after aggregations to put into the unified data.
The function performs aggregations on the list of tests and creates a list of tests in accordance with the desired structure of the unified data.

### aggregateCustomizedData

    input:
        PrismaFile - The java object with the scanner data
    output:
        Map<String, String> -  Map of all customized data for prisma scanner to put into the unified data.
The function performs aggregations on the customized data and create a map of customized data in accordance with the desired structure of the unified data.

### aggregationToUnifiedDataPrismaScanner function:

    input:
      PrismaFile - The java object with the scanner data
      UnifiedDataScanner - To fill into it
    output:
      UnifiedDataScanner - After filling in the relevant details from the prisma scanner file.
The function performs aggregations on the java object that contains data from the file prisma scanner.

### getScannerType

    input:
        No input.
    output:
        ScannerType - ScannerType.PRISMA.
The function returns the scanner type - Prisma.