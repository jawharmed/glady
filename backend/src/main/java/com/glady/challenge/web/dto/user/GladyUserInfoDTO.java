package com.glady.challenge.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GladyUserInfoDTO implements Serializable {

    private Long id;
    private String username;
    private String companyName;
    private double totalBalance;
    private int valideGiftVouchersCount;
    private double giftBalance;
    private int valideMealVouchersCount;
    private double mealBalance;
    private int totalExpiredVouchersCount;
}
