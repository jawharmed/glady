package com.glady.challenge.web.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO implements Serializable {

    @NonNull
    private long id;
    @NonNull
    private String companyName;
    private double mealBalance;
    private double giftBalance;
}
