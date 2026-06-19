package com.rba.rest;

import com.rba.WebContextAwareIT;
import com.rba.common.dto.UserDTO;
import com.rba.common.exceptions.AppError;
import com.rba.common.exceptions.AppException;
import com.rba.rest.utils.ResponseMessage;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerIT extends WebContextAwareIT {

    @Test
    @DirtiesContext
    public void findUserByIdentificationNumber() throws AppException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/users/1234567890"),
                HttpMethod.GET,
                entity,
                String.class
        );

        UserDTO userDTO = getDTOObjectFromBody(response, UserDTO.class);

        assertNotNull(userDTO.getId());
        assertEquals("1", userDTO.getId());
        assertEquals("Mate", userDTO.getFirstName());
        assertEquals("Matić", userDTO.getLastName());
    }

    @Test
    @DirtiesContext
    public void createUserUserAlreadyExists() throws AppException {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Ana");
        userDTO.setLastName("Anic");
        userDTO.setIdentificationNumber("1234567890");
        userDTO.setCardStatus("APPROVED");

        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);

        ResponseEntity<ResponseMessage> response = restTemplate.exchange(
                createURLWithPort("/api/users"),
                HttpMethod.POST,
                entity,
                ResponseMessage.class
        );

        assertEquals(AppError.USER_ALREADY_EXISTS.name(), response.getBody().getErrorCode());
    }

    @Test
    @DirtiesContext
    public void createUser() throws AppException {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Ana");
        userDTO.setLastName("Anic");
        userDTO.setIdentificationNumber("9876543210");
        userDTO.setCardStatus("APPROVED");

        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/users"),
                HttpMethod.POST,
                entity,
                String.class
        );

        UserDTO result = getDTOObjectFromBody(response, UserDTO.class);

        assertEquals("Ana", result.getFirstName());
        assertEquals("Anic", result.getLastName());
        assertEquals("9876543210", result.getIdentificationNumber());
    }

    @Test
    @DirtiesContext
    public void createUserIdentificationNumberBadRequest() throws AppException {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Ana");
        userDTO.setLastName("Anic");
        userDTO.setCardStatus("APPROVED");

        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);

        ResponseEntity<ResponseMessage> response = restTemplate.exchange(
                createURLWithPort("/api/users"),
                HttpMethod.POST,
                entity,
                ResponseMessage.class
        );

        assertEquals(AppError.BAD_REQUEST.name(), response.getBody().getErrorCode());
    }

    @Test
    @DirtiesContext
    public void createUserCardStatusBadRequest() throws AppException {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Ana");
        userDTO.setLastName("Anic");
        userDTO.setIdentificationNumber("9876543210");

        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);

        ResponseEntity<ResponseMessage> response = restTemplate.exchange(
                createURLWithPort("/api/users"),
                HttpMethod.POST,
                entity,
                ResponseMessage.class
        );

        assertEquals(AppError.BAD_REQUEST.name(), response.getBody().getErrorCode());
    }

    @Test
    @DirtiesContext
    public void deleteUser() {
        HttpEntity<UserDTO> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessage> response = restTemplate.exchange(
                createURLWithPort("/api/users/1234567890"),
                HttpMethod.DELETE,
                entity,
                ResponseMessage.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    public void deleteNonExistingUser() {
        HttpEntity<UserDTO> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseMessage> response = restTemplate.exchange(
                createURLWithPort("/api/users/0"),
                HttpMethod.DELETE,
                entity,
                ResponseMessage.class
        );

        assertEquals(AppError.USER_NOT_FOUND.name(), response.getBody().getErrorCode());
    }
}