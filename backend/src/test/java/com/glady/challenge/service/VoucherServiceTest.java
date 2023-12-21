package com.glady.challenge.service;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.repository.VoucherRepository;
import com.glady.challenge.service.wallet.VoucherService;
import com.glady.challenge.web.dto.wallet.VoucherDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @Spy
    @InjectMocks
    private VoucherService voucherService;

    @Test
    void testCreate_GivenNonExistingVoucherGift_ShouldCreateNewVoucherGift(){
        VoucherDTO voucherDTO = DtoObjectHelper.getVoucherGiftDTO();
        Wallet wallet = ObjectHelper.getWalletGift();
        when(voucherRepository.findByCodeIgnoreCase(voucherDTO.getCode())).thenReturn(Optional.empty());
        when(voucherRepository.save(Mockito.any())).thenAnswer(response -> response.getArgument(0));
        Voucher result = voucherService.create(voucherDTO, wallet);

        assertNotNull(result);
        assertEquals(voucherDTO.getCode(), result.getCode());
        assertEquals(voucherDTO.getWalletId(), result.getWallet().getId());

        verify(voucherRepository, times(1)).save(any());
    }

    @Test
    void testCreate_GivenAnExistingVoucher_ShouldThrowEntityAlreadyExistException(){
        VoucherDTO voucherDTO = DtoObjectHelper.getVoucherGiftDTO();
        Wallet wallet = ObjectHelper.getWalletGift();
        when(voucherRepository.findByCodeIgnoreCase(voucherDTO.getCode())).thenReturn(Optional.of(new Voucher()));

        assertThrows(EntityAlreadyExistException.class, () -> voucherService.create(voucherDTO, wallet));
        verify(voucherRepository, never()).save(any());
    }
}
