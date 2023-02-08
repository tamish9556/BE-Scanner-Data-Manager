package com.dashbord.IntregrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public abstract class IntegrationTesting {

    @LocalServerPort
    int port;
    @Autowired
    TestRestTemplate restTemplate;
    HttpHeaders headers = null;
    MultiValueMap<String, Object> body = null;
    protected String createURLWithPort(String uri) {

        return "http://localhost:" + port + uri;
    }
}
