package com.rba.common.utils.rest;

import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestParameters {

    private HttpHeaders headers;

    private String url;

    private Object body;

    private HttpMethod method;
}
