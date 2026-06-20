package com.rba.services.user;

import com.rba.common.dto.UserDTO;
import com.rba.common.exceptions.AppError;
import com.rba.common.exceptions.AppException;
import com.rba.common.mappers.UserMapper;
import com.rba.common.utils.rest.RestParameters;
import com.rba.common.utils.rest.RestUtil;
import com.rba.domain.User;
import com.rba.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    public static void assertException(AppError error, Executable executable) {
        AppException exception = assertThrows(AppException.class, executable);
        assertEquals(error, exception.getError());
    }

    @Test
    void createUserIdentificationNumberBadRequest() {
        UserDTO userDTO = getUserDTOData();
        userDTO.setIdentificationNumber(null);

        assertException(AppError.BAD_REQUEST, () -> userService.createUser(userDTO));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUserCardStatusBadRequest() {
        UserDTO userDTO = getUserDTOData();
        userDTO.setCardStatus(null);

        assertException(AppError.BAD_REQUEST, () -> userService.createUser(userDTO));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUserUserAlreadyExists() {
        UserDTO userDTO = getUserDTOData();
        when(userRepository.existsByIdentificationNumber("12345678901")).thenReturn(true);

        assertException(AppError.USER_ALREADY_EXISTS, () -> userService.createUser(userDTO));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUserSuccess() throws AppException {
        UserDTO userDTO = getUserDTOData();
        when(userRepository.existsByIdentificationNumber("12345678901")).thenReturn(false);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertThat(result).isEqualTo(userDTO);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertThat(saved.getIdentificationNumber()).isEqualTo("12345678901");
        assertThat(saved.getFirstName()).isEqualTo("Ana");
        assertThat(saved.getLastName()).isEqualTo("Anic");
        assertThat(saved.getCardStatus()).isEqualTo("APPROVED");
    }

    @Test
    void getUserUserNotFound() {
        when(userRepository.findUserByIdentificationNumber("99999999999")).thenReturn(Optional.empty());

        UserDTO result = userService.getUserByIdentificationNumber("99999999999");

        assertThat(result).isNull();
        verify(userMapper, never()).userToUserDTO(any());
    }

    @Test
    void getUserSuccess() {
        UserDTO userDTO = getUserDTOData();
        User user = getUserData();
        when(userRepository.findUserByIdentificationNumber("12345678901")).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        try (MockedStatic<RestUtil> restUtil = mockStatic(RestUtil.class)) {
            restUtil.when(() -> RestUtil.performRequest(any(RestParameters.class)))
                    .thenReturn(ResponseEntity.ok("OK"));

            UserDTO result = userService.getUserByIdentificationNumber("12345678901");

            assertThat(result).isEqualTo(userDTO);
            restUtil.verify(() -> RestUtil.performRequest(any(RestParameters.class)), times(1));
        }
    }

    @Test
    void deleteUserUserNotFound() {
        when(userRepository.findUserByIdentificationNumber("99999999999")).thenReturn(Optional.empty());

        assertException(AppError.USER_NOT_FOUND, () -> userService.deleteUser("99999999999"));

        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteUserSuccess() throws AppException {
        User user = getUserData();
        when(userRepository.findUserByIdentificationNumber("12345678901")).thenReturn(Optional.of(user));

        userService.deleteUser("12345678901");

        verify(userRepository).delete(user);
    }

    @Test
    void updateUserIdentificationNumberBadRequest() {
        UserDTO userDTO = getUserDTOData();
        userDTO.setIdentificationNumber(null);

        assertException(AppError.BAD_REQUEST, () -> userService.updateUser(userDTO));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserUserNotFound() {
        UserDTO userDTO = getUserDTOData();

        when(userRepository.findUserByIdentificationNumber("12345678901"))
                .thenReturn(Optional.empty());

        assertException(AppError.USER_NOT_FOUND, () -> userService.updateUser(userDTO));

        verify(userRepository, never()).save(any());
        verify(userMapper, never()).userToUserDTO(any());
    }

    @Test
    void updateCardStatusByIdentficationNumberSuccess() {
        User user = getUserData();
        user.setCardStatus("PENDING");

        when(userRepository.findUserByIdentificationNumber("12345678901"))
                .thenReturn(Optional.of(user));

        userService.updateCardStatusByIdentficationNumber("12345678901", "DELIVERED");

        assertThat(user.getIdentificationNumber()).isEqualTo("12345678901");
        assertThat(user.getCardStatus()).isEqualTo("DELIVERED");

        verify(userRepository).save(user);
    }

    private User getUserData() {
        User user = new User();
        user.setIdentificationNumber("12345678901");
        user.setFirstName("Ana");
        user.setLastName("Anic");
        user.setCardStatus("APPROVED");

        return user;

    }

    private UserDTO getUserDTOData() {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdentificationNumber("12345678901");
        userDTO.setFirstName("Ana");
        userDTO.setLastName("Anic");
        userDTO.setCardStatus("APPROVED");

        return userDTO;

    }
}