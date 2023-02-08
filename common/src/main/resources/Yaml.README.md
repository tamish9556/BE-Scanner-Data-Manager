created a yaml file by the Name of ScannerDataManager.yml
witch contains all end points specification
and model schemas for the entire application.
running mvn clean install command 
will generate the  interfaces of the RESTApi controllers
and the classes specified in schemas.
generated code will be found in target under generated-source folder
swagger ui can be run on browser by following path:
http://localhost:8080/swagger-ui/index.html