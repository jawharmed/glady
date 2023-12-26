package com.glady.challenge.web.dto.deposit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositDTO implements Serializable {

    private Long id;

    @Min(0)
    private double amount;

    @NotEmpty
    private String depositType;

    @NotNull
    private Long companyId;

    @NotNull
    private Long gladyUserId;
}
