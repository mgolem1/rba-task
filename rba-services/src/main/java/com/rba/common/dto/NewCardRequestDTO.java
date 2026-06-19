package com.rba.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCardRequestDTO {

    private String firstName;

    private String lastName;

    @JsonProperty(value = "status")
    private String cardStatus;

    @JsonProperty(value = "oib")
    private String identificationNumber;
}
