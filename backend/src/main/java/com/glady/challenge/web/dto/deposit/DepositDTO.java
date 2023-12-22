package com.glady.challenge.web.dto.deposit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositDTO implements Serializable {

    private Long id;
    @Min(value = 0, message="must be greater than 0")
    private double amount;
    private String depositType;
    private Long companyId;
    private Long gladyUserId;
}
