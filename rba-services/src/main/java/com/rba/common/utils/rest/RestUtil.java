package com.rba.common.utils.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestUtil {

    public static ResponseEntity<String> performRequest(RestParameters restParameters) {

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Object> entity = new HttpEntity<>(
                restParameters.getBody(),
                restParameters.getHeaders()
        );

        return restTemplate.exchange(
                restParameters.getUrl(),
                restParameters.getMethod(),
                entity,
                String.class
        );
    }
}