package com.rba.common.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private String id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(max = 128)
    private String firstName;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(max = 128)
    private String lastName;

    private String identificationNumber;

    private String cardStatus;

}
