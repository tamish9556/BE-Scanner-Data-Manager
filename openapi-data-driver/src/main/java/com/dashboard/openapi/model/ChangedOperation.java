
package com.dashboard.openapi.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class ChangedOperation {
    private Operation oldOperation;
    private Operation newOperation;
    private String pathUrl;
    private String httpMethod;
    private String summary;
    private Operation operation;
}
