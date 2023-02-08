# scanner-data-manager


#dynamic scanners - 
In addition to the scanners handled by the application, 
there is an option to connect to various applications that perform parsing and aggregation for additional scanners
To use this feature follow these steps:

1.open application.properties file and add your scanner name with the url to parsing function in the additional application.
for example:
dynmcscn.mapScannerToUrl.scannerName=http://localhost:8080/scannerName/parsing.

2.Make sure the parsing function is of type POST,
receives: a MultipartFile type file and a string that is the UnifiedData object.
The function maps the string to a UnifiedData object, 
Fill in the data with the information in the file
and return the unifiedData object which is full in ResponseEntity body.
for example:
@RequestMapping(
           method = RequestMethod.POST,
           produces = { "*/*", "application/json" }
)
public ResponseEntity<UnifiedDataScanner> uploadScannerFile(
          @RequestPart MultipartFile file,
          @RequestParam String unifiedData
){
            //some parsing code here
        return ResponseEntity.ok().body(unifiedData);
}
