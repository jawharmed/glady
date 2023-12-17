package com.glady.challenge.web.dto.deposit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositDTO implements Serializable {

    private Long id;
    private double amount;
    private String depositType;
    private Long companyId;
    private Long gladyUserId;
}
