package com.rba.services.user;

import com.rba.common.dto.CardInformationRequestDTO;
import com.rba.common.dto.UserDTO;
import com.rba.common.exceptions.AppError;
import com.rba.common.exceptions.AppException;
import com.rba.common.mappers.UserMapper;
import com.rba.common.utils.rest.RestParameters;
import com.rba.common.utils.rest.RestUtil;
import com.rba.domain.User;
import com.rba.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Value("${card.api.url}")
    private String cardApiUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(UserDTO userDTO) throws AppException {

        if (userDTO.getIdentificationNumber() == null) {
            throw new AppException(AppError.BAD_REQUEST, "Identification number is required!");
        }

        if (userDTO.getCardStatus() == null) {
            throw new AppException(AppError.BAD_REQUEST, "Card status is required!");
        }

        if (userRepository.existsByIdentificationNumber(userDTO.getIdentificationNumber())) {
            log.info("User with identification number {} already exists", userDTO.getIdentificationNumber());
            throw new AppException(AppError.USER_ALREADY_EXISTS, String.format("User with identification number %s already exists!", userDTO.getIdentificationNumber()));
        }

        User user = new User();
        user.setIdentificationNumber(userDTO.getIdentificationNumber());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setCardStatus(userDTO.getCardStatus());

        userRepository.save(user);

        return userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO getUserByIdentificationNumber(String identificationNumber) {

        Optional<User> userOptional = userRepository.findUserByIdentificationNumber(identificationNumber);

        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();

        sendUserData(user);

        return userMapper.userToUserDTO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO updateUser(UserDTO userDTO) throws AppException {

        if (userDTO.getIdentificationNumber() == null) {
            throw new AppException(AppError.BAD_REQUEST, "Identification number is required!");
        }
        Optional<User> userOptional = userRepository.findUserByIdentificationNumber(userDTO.getIdentificationNumber());

        if (userOptional.isEmpty()) {
            log.info("User with OIB {} does not exist", userDTO.getIdentificationNumber());
            throw new AppException(AppError.USER_NOT_FOUND, String.format("User with OIB %s does not exist!", userDTO.getIdentificationNumber()));
        }

        User user = userOptional.get();
        user.setIdentificationNumber(userDTO.getIdentificationNumber());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setCardStatus(userDTO.getCardStatus());

        userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String identificationNumber) throws AppException {

        Optional<User> userOptional = userRepository.findUserByIdentificationNumber(identificationNumber);

        if (userOptional.isEmpty()) {
            log.info("User with OIB {} does not exist", identificationNumber);
            throw new AppException(AppError.USER_NOT_FOUND, String.format("User with OIB %s does not exist!", identificationNumber));
        }

        userRepository.delete(userOptional.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCardStatusByIdentficationNumber(String identificationNumber, String cardStatus) {

        Optional<User> userOptional = userRepository.findUserByIdentificationNumber(identificationNumber);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIdentificationNumber(identificationNumber);
            user.setCardStatus(cardStatus);

            userRepository.save(user);
        }
    }

    public void sendUserData(User user) {

        CardInformationRequestDTO cardInformationRequestDTO = new CardInformationRequestDTO();
        cardInformationRequestDTO.setFirstName(user.getFirstName());
        cardInformationRequestDTO.setLastName(user.getLastName());
        cardInformationRequestDTO.setCardStatus(user.getCardStatus());
        cardInformationRequestDTO.setIdentificationNumber(user.getIdentificationNumber());

        ResponseEntity<String> response;

        try {
            response = RestUtil.performRequest(
                    RestParameters.builder()
                            .headers(new HttpHeaders())
                            .body(cardInformationRequestDTO)
                            .url(String.format("%s%s", cardApiUrl, "v1/api/v1/card-request"))
                            .method(HttpMethod.POST)
                            .build());

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
            log.warn("Failed sending data to card API for user {}", user.getIdentificationNumber());
                throw new AppException(AppError.SENDING_DATA_TO_CARD_API_FAILED, String.format("Sending data to card API for user %s failed with status %s.", user.getIdentificationNumber(), response.getStatusCode()));
            }
            log.info("Successfully send user data {} to card API", user.getIdentificationNumber());

        } catch (Exception e) {
            log.error("Failed to send user data {} to card API: {}", user.getIdentificationNumber(), e.getMessage());

        }
    }
}
