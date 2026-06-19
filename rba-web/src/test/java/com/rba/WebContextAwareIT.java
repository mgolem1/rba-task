package com.rba;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.rba.common.exceptions.AppException;
import com.rba.common.utils.JsonSimpleHelper;
import com.rba.rest.utils.ResponseMessage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = RbaWebApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "app.kafka.enabled=false",
                "spring.kafka.admin.auto-create=false"
        }
)
public class WebContextAwareIT {

        protected RestTemplate restTemplate = new RestTemplate();

        protected HttpHeaders headers = new HttpHeaders();

        protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        @LocalServerPort
        private int port;

        protected String createURLWithPort(String uri) {
            return "http://localhost:" + port + uri;
        }

        protected <T> List<T> getPageFromBody(ResponseEntity<String> response, Class<T> dTOClass) throws AppException {
            ResponseMessage message = JsonSimpleHelper.deserialise(response.getBody(), ResponseMessage.class);

            TypeReference<List<T>> valueTypeRef = new TypeReference<List<T>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            };

            JsonNode tree = JsonSimpleHelper.turnIntoJsonTree(response.getBody());

            List<T> resultList = new ArrayList<>();
            JsonNode content = tree.get("payload").get("content");

            for (int i = 0; i < content.size(); i++) {
                JsonNode node = content.get(i);
                resultList.add(JsonSimpleHelper.deserialise(node, dTOClass));
            }

            return resultList;
        }

        protected <T> T getDTOObjectFromBody(ResponseEntity<String> response, Class<T> dTOClass) throws AppException {
            ResponseMessage message = JsonSimpleHelper.deserialise(response.getBody(), ResponseMessage.class);

            JsonNode tree = JsonSimpleHelper.turnIntoJsonTree(response.getBody());

            return JsonSimpleHelper.deserialise(tree.get("payload"), dTOClass);
        }
    }