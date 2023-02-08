package com.dashboard.prisma.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskFactors {
    private AttackComplexityLow attackComplexityLow;
    private AttackVectorNetwork attackVectorNetwork;
    private DoS doS;
    private HasFix hasFix;
    private HighSeverity highSeverity;
    private RecentVulnerability recentVulnerability;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}

