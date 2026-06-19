package com.rba.rest.controller;

import com.rba.common.dto.UserDTO;
import com.rba.common.exceptions.AppException;
import com.rba.rest.utils.ResponseMessage;
import com.rba.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseMessage<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) throws AppException {
        return ResponseEntity.ok(new ResponseMessage<>(userService.createUser(userDTO)));
    }

    @GetMapping("/{identificationNumber}")
    public ResponseEntity<ResponseMessage<UserDTO>> getUserByIdentificationNumber(@PathVariable String identificationNumber) {
        return ResponseEntity.ok(new ResponseMessage<>(userService.getUserByIdentificationNumber(identificationNumber)));
    }

    @DeleteMapping("/{identificationNumber}")
    public ResponseEntity<ResponseMessage<String>> deleteUser(@PathVariable String identificationNumber) throws AppException {
        userService.deleteUser(identificationNumber);
        return ResponseEntity.ok(new ResponseMessage<>(""));
    }
}
