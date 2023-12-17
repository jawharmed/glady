package com.glady.challenge.web.dto.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO implements Serializable {

    private Long id;
    private String walletType;
    private Long gladyUserId;
}
