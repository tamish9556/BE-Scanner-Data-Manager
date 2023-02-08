package com.dashboard.junit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="testsuite")
public class Testsuite {
    @XmlAttribute
    public String name;
    @XmlAttribute
    public String time;
    @XmlElement
    private List<TestCase> testcase;
}

