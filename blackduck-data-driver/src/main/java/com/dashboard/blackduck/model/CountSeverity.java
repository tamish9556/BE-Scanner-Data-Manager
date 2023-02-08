package com.dashboard.blackduck.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountSeverity {
    Integer countHigh=0;
    Integer countLow=0;
    Integer countMedium=0;
    Integer countTests=0;
}
