package com.rba.services.user;

import com.rba.common.exceptions.AppError;
import com.rba.common.exceptions.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AppWebContextAwareUT {
    @BeforeEach
    public void setUp() {
    }

    public static void assertException(AppError error, Executable executable) {
        AppException exception = assertThrows(AppException.class, executable);
        assertEquals(error, exception.getError());
    }

}