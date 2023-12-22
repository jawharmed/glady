package com.glady.challenge.service;

import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.repository.WalletRepository;
import com.glady.challenge.service.gladyuser.GladyUserService;
import com.glady.challenge.service.wallet.WalletService;
import com.glady.challenge.web.dto.wallet.WalletDTO;
import com.glady.challenge.web.mapper.WalletMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private GladyUserService gladyUserService;

    @Spy
    @InjectMocks
    private WalletService walletService;

    @Test
    void testGetById_GivenExistingWalletId_ShouldReturnWallet(){
        Long walletId = 2L;
        Wallet expected = ObjectHelper.getWalletGift();
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(expected));

        Wallet result = walletService.getById(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getId());
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testGetById_GivenNotExistingWalletId_ShouldThrowEntityNotFoundException(){
        Long walletId = 0L;
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> walletService.getById(walletId));
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testGetDtoById_GivenExistingWalletId_ShouldReturnWalletDto(){
        Long walletId = 2L;
        Wallet expected = ObjectHelper.getWalletGift();
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(expected));

        WalletDTO result = walletService.getDtoById(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getId());
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testGetDtoById_GivenNotExistingWalletId_ShouldThrowEntityNotFoundException(){
        Long walletId = 0L;
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> walletService.getDtoById(walletId));
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testGetOrCreate_GivenExistingWallet_ShouldReturnWallet(){
        WalletDTO givenWalletDTO = DtoObjectHelper.getWalletGiftDTO();
        VoucherTypeEnum givenVoucherType = VoucherTypeEnum.valueOfType(givenWalletDTO.getWalletType());
        GladyUser gladyUser = ObjectHelper.getGladyUser();
        givenWalletDTO.setGladyUserId(gladyUser.getId());
        Wallet expectedWallet = WalletMapper.INSTANCE.toWallet(givenWalletDTO, gladyUser);

        when(walletRepository.findByGladyUserIdAndWalletType(givenWalletDTO.getGladyUserId(), givenVoucherType)).thenReturn(Optional.of(expectedWallet));

        Wallet result = walletService.getOrCreate(givenWalletDTO);

        assertNotNull(result);
        assertEquals(givenWalletDTO.getId(), result.getId());
        assertEquals(givenWalletDTO.getWalletType(), result.getWalletType().getType());
        assertEquals(givenWalletDTO.getGladyUserId(), result.getGladyUser().getId());

        verify(walletRepository, times(1)).findByGladyUserIdAndWalletType(anyLong(), any());
    }

    @Test
    void testGetOrCreate_GivenNonExistingWallet_ShouldCreateNewWallet(){
        WalletDTO givenWalletDTO = DtoObjectHelper.getWalletGiftDTO();
        givenWalletDTO.setId(null);
        VoucherTypeEnum givenVoucherType = VoucherTypeEnum.valueOfType(givenWalletDTO.getWalletType());
        GladyUser gladyUser = ObjectHelper.getGladyUser();
        givenWalletDTO.setGladyUserId(gladyUser.getId());

        when(walletRepository.findByGladyUserIdAndWalletType(givenWalletDTO.getGladyUserId(), givenVoucherType)).thenReturn(Optional.empty());
        when(gladyUserService.getById(givenWalletDTO.getGladyUserId())).thenReturn(gladyUser);
        when(walletRepository.save(any())).thenAnswer(response -> response.getArgument(0));

        Wallet result = walletService.getOrCreate(givenWalletDTO);

        assertNotNull(result);
        assertEquals(givenWalletDTO.getGladyUserId(), result.getGladyUser().getId());

        verify(walletRepository, times(1)).findByGladyUserIdAndWalletType(anyLong(), any());
        verify(walletRepository, times(1)).save(any());
        verify(gladyUserService, times(1)).getById(anyLong());
    }


}
