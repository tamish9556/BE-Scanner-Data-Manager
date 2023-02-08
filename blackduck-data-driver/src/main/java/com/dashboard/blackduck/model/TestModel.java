package com.dashboard.blackduck.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class TestModel {
    String severity;
    String vulnerabilityName;
    String description;
    String vulnerabilityPublishedDate;
    String vulnerabilityUpdatedDate;
    String authentication;
    String baseScore;
    String impactSubscore;
    String exploitabilitySubscore;
    String source;
    String accessVector;
    String accessComplexity;
    String confidentialityImpact;
    String integrityImpact;
    String availabilityImpact;
    String cweId;



}
