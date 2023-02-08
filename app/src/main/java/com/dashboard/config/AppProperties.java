package com.dashboard.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("dynmcscn")
@Getter
@Setter
public class AppProperties {
    private Map<String, String> mapScannerToUrl;
}
