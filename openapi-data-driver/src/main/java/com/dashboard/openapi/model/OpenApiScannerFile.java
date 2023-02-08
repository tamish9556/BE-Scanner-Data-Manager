
package com.dashboard.openapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenApiScannerFile {
    private SpecOpenApi oldSpecOpenApi;
    private SpecOpenApi newSpecOpenApi;
    private List<ChangedOperation> newEndpoints = null;
    private List<ChangedOperation> missingEndpoints = null;
    private List<ChangedOperation> changedOperations = null;
    private List<ChangedOperation> changedSchemas = null;
    private List<ChangedOperation> changedElements = null;
    private List<ChangedOperation> deprecatedEndpoints = null;

}
