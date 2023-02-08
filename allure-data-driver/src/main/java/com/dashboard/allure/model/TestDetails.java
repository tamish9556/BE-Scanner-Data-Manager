package com.dashboard.allure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDetails {
    private String name;
    private String uid;
    private String parentUid;
    private String status;
    private Time time;
    private Boolean flaky;
    private Boolean newFailed;
    private List<String> parameters = null;
}
