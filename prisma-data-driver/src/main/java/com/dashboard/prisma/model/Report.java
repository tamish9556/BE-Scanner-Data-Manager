package com.dashboard.prisma.model;
import java.util.HashMap;
import java.util.List;
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
public class Report {
    private String text;
    private Integer id;
    private String severity;
    private Double cvss;
    private String status;
    private String cve;
    private String cause;
    private String description;
    private String title;
    private String vecStr;
    private String exploit;
    private RiskFactors riskFactors;
    private String link;
    private String type;
    private String packageName;
    private String packageVersion;
    private Integer layerTime;
    private Object templates;
    private Boolean twistlock;
    private Boolean cri;
    private Integer published;
    private Integer fixDate;
    private List<String> applicableRules = null;
    private String discovered;
    private String functionLayer;
    private Map<String, Object> additionalProperties = new HashMap<>();

}

