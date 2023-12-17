package com.glady.challenge.web.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO implements Serializable {

    private long id;
    private double amount;
    private ZonedDateTime createdOn;
    private ZonedDateTime expiresOn;
    private String receivedFrom;
    private String code;
    private Long walletId;
}
