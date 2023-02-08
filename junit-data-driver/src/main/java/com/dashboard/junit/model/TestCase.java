package com.dashboard.junit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class TestCase {
    @XmlAttribute
    public String name;
    @XmlAttribute
    public String classname;
    @XmlAttribute
    private String time;
    @XmlElement
    public Failure failure;
    @XmlElement(name = "system-out")
    public String systemOut;
}


