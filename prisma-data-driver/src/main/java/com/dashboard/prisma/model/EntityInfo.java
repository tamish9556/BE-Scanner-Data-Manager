package com.dashboard.prisma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityInfo {
    List<Report> vulnerabilities;
    String _id;
    String type;
    String hostname;
    Date scanTime;
    String onDistro;
    String onDistroVersion;
    String OnDistroRelease;
    String distro;
    Boolean packageManager;
    String id;
    Date creationTime;
    Integer vulnerabilitiesCount;
    Integer complianceIssuesCount;
    Integer vulnerabilitiesRiskScore;
    Integer complianceRiskScore;
    String topLayer;
    String scanVersion;
    Date firstScanTime;
    String err;
    Integer scanID;
    String trustStatus;
    Boolean appEmbedded;
}
