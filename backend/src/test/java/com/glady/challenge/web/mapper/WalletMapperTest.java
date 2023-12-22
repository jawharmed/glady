package com.glady.challenge.web.mapper;

import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.web.dto.wallet.WalletDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class WalletMapperTest {

    private final WalletMapper walletMapper = WalletMapper.INSTANCE;

    @Test
    void testWalletToWalletDTO_Meal() {
        Wallet wallet = ObjectHelper.getWalletMeal();

        WalletDTO walletDTO = walletMapper.toWalletDTO(wallet);

        assertNotNull(walletDTO);
        assertEquals(wallet.getId(), walletDTO.getId());
        assertEquals(wallet.getWalletType().getType(), walletDTO.getWalletType());
        assertEquals(wallet.getGladyUser().getId(), walletDTO.getGladyUserId());
    }

    @Test
    void testWalletDtoToWallet_Meal() {
        WalletDTO walletDTO = DtoObjectHelper.getWalletMealDTO();
        GladyUser gladyUser = ObjectHelper.getGladyUser();
        walletDTO.setGladyUserId(gladyUser.getId());

        Wallet wallet = walletMapper.toWallet(walletDTO, gladyUser);

        assertNotNull(wallet);
        assertEquals(walletDTO.getId(), wallet.getId());
        assertEquals(walletDTO.getWalletType(), wallet.getWalletType().getType());
        assertEquals(walletDTO.getGladyUserId(), wallet.getGladyUser().getId());
    }

    @Test
    void testWalletToWalletDTO_Meal_When_Wallet_Is_Null() {
        WalletDTO walletDTO = walletMapper.toWalletDTO(null);
        assertNull(walletDTO);
    }

    @Test
    void testWalletDtoToWallet_Meal_When_Wallet_Is_Null() {
        Wallet wallet = walletMapper.toWallet(null, null);
        assertNull(wallet);
    }

    @Test
    void testWalletToWalletDTO_Meal_When_GladyUser_Is_Null() {
        Wallet wallet = ObjectHelper.getWalletMeal();
        wallet.setGladyUser(null);
        WalletDTO walletDTO = walletMapper.toWalletDTO(wallet);

        assertNotNull(walletDTO);
        assertEquals(wallet.getId(), walletDTO.getId());
        assertEquals(wallet.getWalletType().getType(), walletDTO.getWalletType());
        assertNull(walletDTO.getGladyUserId());
    }

    @Test
    void testWalletDtoToWallet_Meal_When_GladyUser_Is_Null() {
        WalletDTO walletDTO = DtoObjectHelper.getWalletMealDTO();
        walletDTO.setGladyUserId(null);
        Wallet wallet = walletMapper.toWallet(walletDTO, null);

        assertNotNull(wallet);
        assertEquals(walletDTO.getId(), wallet.getId());
        assertEquals(walletDTO.getWalletType(), wallet.getWalletType().getType());
        assertNull(wallet.getGladyUser());
    }

}
