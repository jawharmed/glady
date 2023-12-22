package com.glady.challenge.helpers;

import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.web.dto.company.CompanyDTO;
import com.glady.challenge.web.dto.deposit.DepositDTO;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import com.glady.challenge.web.dto.wallet.VoucherDTO;
import com.glady.challenge.web.dto.wallet.WalletDTO;

import java.time.ZonedDateTime;

public class DtoObjectHelper {

    public static CompanyDTO getCompanyDTO() {
        return CompanyDTO.builder()
                .id(1L)
                .companyName("Apple")
                .mealBalance(1000.00)
                .giftBalance(500.00)
                .build();
    }

    public static GladyUserDTO getGladyUserDTO(){
        return GladyUserDTO.builder()
                .id(1L)
                .username("Jessica")
                .companyId(1L)
                .build();
    }

    public static WalletDTO getWalletMealDTO(){
        return WalletDTO.builder()
                .id(1L)
                .walletType(VoucherTypeEnum.MEAL.getType())
                .gladyUserId(1L)
                .build();
    }

    public static WalletDTO getWalletGiftDTO(){
        return WalletDTO.builder()
                .id(1L)
                .walletType(VoucherTypeEnum.GIFT.getType())
                .gladyUserId(1L)
                .build();
    }

    public static VoucherDTO getVoucherGiftDTO(){
        return VoucherDTO.builder()
                .id(1L)
                .amount(100)
                .code("G-CODE-1")
                .createdOn(ZonedDateTime.now())
                .expiresOn(ZonedDateTime.now().plusDays(365))
                .walletId(getWalletGiftDTO().getId())
                .receivedFrom(getCompanyDTO().getCompanyName())
                .build();
    }

    public static DepositDTO getDepositMealDTO(){
        return DepositDTO.builder()
                .id(1L)
                .depositType(VoucherTypeEnum.MEAL.getType())
                .amount(150)
                .companyId(1L)
                .gladyUserId(1L)
                .build();
    }

    public static DepositDTO getDepositGiftDTO(){
        return DepositDTO.builder()
                .id(1L)
                .depositType(VoucherTypeEnum.GIFT.getType())
                .amount(50)
                .companyId(1L)
                .gladyUserId(1L)
                .build();
    }
}
