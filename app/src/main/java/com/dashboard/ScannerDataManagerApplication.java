package com.dashboard;
import com.dashboard.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
public class ScannerDataManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScannerDataManagerApplication.class, args);
    }

}