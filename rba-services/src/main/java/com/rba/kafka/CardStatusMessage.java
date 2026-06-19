package com.rba.kafka;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardStatusMessage {

    private String oib;

    private String status;
}
