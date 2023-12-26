package com.glady.challenge.web.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO implements Serializable {

    private Long id;

    @NotEmpty
    private String companyName;

    @Min(0)
    private double mealBalance;

    @Min(0)
    private double giftBalance;
}
