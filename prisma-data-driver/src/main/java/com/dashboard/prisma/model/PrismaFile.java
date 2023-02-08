package com.dashboard.prisma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrismaFile {
    EntityInfo entityInfo;
    String _id;
    Date time;
    String jobName;
    String build;
    Boolean pass;
    String version;
}
