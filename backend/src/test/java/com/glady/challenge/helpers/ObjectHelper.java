package com.glady.challenge.helpers;

import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.deposit.Deposit;
import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.model.wallet.Wallet;

import java.time.ZonedDateTime;

public class ObjectHelper {

    public static Company getCompany() {
        return Company.builder()
                .id(1L)
                .companyName("Apple")
                .mealBalance(100.00)
                .giftBalance(50.00)
                .build();
    }

    public static GladyUser getGladyUser(){
        return GladyUser.builder()
                .id(1L)
                .username("Jessica")
                .company(getCompany())
                .build();
    }

    public static Wallet getWalletMeal(){
        return Wallet.builder()
                .id(1L)
                .walletType(VoucherTypeEnum.MEAL)
                .gladyUser(getGladyUser())
                .build();
    }

    public static Wallet getWalletGift(){
        return Wallet.builder()
                .id(1L)
                .walletType(VoucherTypeEnum.GIFT)
                .gladyUser(getGladyUser())
                .build();
    }

    public static Voucher getVoucherGift(){
        return Voucher.builder()
                .id(1L)
                .amount(getDepositGift().getAmount())
                .code("MEAL-CODE-1")
                .createdOn(ZonedDateTime.now())
                .expiresOn(ZonedDateTime.now().plusDays(365))
                .wallet(getWalletGift())
                .receivedFrom(getCompany().getCompanyName())
                .build();
    }

    public static Deposit getDepositMeal(){
        return Deposit.builder()
                .id(1L)
                .depositType(VoucherTypeEnum.MEAL)
                .amount(150)
                .company(getCompany())
                .gladyUser(getGladyUser())
                .build();
    }

    public static Deposit getDepositGift(){
        return Deposit.builder()
                .id(1L)
                .depositType(VoucherTypeEnum.GIFT)
                .amount(500)
                .company(getCompany())
                .gladyUser(getGladyUser())
                .build();
    }
}
