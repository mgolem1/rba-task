package com.rba.services.user;

import com.rba.common.dto.UserDTO;
import com.rba.common.exceptions.AppException;

public interface UserService {

    UserDTO createUser(UserDTO userDTO) throws AppException;

    UserDTO getUserByIdentificationNumber(String identificationNumber);

    UserDTO updateUser(UserDTO userDTO) throws AppException;

    void deleteUser(String identificationNumber) throws AppException;

    void updateCardStatusByIdentficationNumber(String identificationNumber, String cardStatus);
}
