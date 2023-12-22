package com.glady.challenge.web.mapper;

import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.web.dto.wallet.VoucherDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class VoucherMapperTest {

    private final VoucherMapper voucherMapper = VoucherMapper.INSTANCE;

    @Test
    void testVoucherToVoucherDto_Gift(){
        Voucher voucher = ObjectHelper.getVoucherGift();

        VoucherDTO voucherDTO = voucherMapper.toVoucherDTO(voucher);

        assertNotNull(voucherDTO);
        assertEquals(voucher.getId(), voucherDTO.getId());
        assertEquals(voucher.getCode(), voucherDTO.getCode());
        assertEquals(voucher.getAmount(), voucherDTO.getAmount());
        assertEquals(voucher.getCreatedOn(), voucherDTO.getCreatedOn());
        assertEquals(voucher.getExpiresOn(), voucherDTO.getExpiresOn());
        assertEquals(voucher.getReceivedFrom(), voucherDTO.getReceivedFrom());
        assertEquals(voucher.getWallet().getId(), voucherDTO.getWalletId());
    }

    @Test
    void testVoucherDtoToVoucher_Gift(){
        VoucherDTO voucherDTO = DtoObjectHelper.getVoucherGiftDTO();
        Wallet wallet = ObjectHelper.getWalletGift();
        voucherDTO.setWalletId(wallet.getId());

        Voucher voucher = voucherMapper.toVoucher(voucherDTO, wallet);

        assertNotNull(voucher);
        assertEquals(voucherDTO.getId(), voucher.getId());
        assertEquals(voucherDTO.getCode(), voucher.getCode());
        assertEquals(voucherDTO.getAmount(), voucher.getAmount());
        assertEquals(voucherDTO.getCreatedOn(), voucher.getCreatedOn());
        assertEquals(voucherDTO.getExpiresOn(), voucher.getExpiresOn());
        assertEquals(voucherDTO.getReceivedFrom(), voucher.getReceivedFrom());
        assertEquals(voucherDTO.getWalletId(), voucher.getWallet().getId());
    }

    @Test
    void testVoucherToVoucherDto_Gift_When_Voucher_Is_Null() {
        VoucherDTO voucherDTO = voucherMapper.toVoucherDTO(null);
        assertNull(voucherDTO);
    }

    @Test
    void testVoucherDtoToVoucher_Gift_When_VoucherDto_Is_Null() {
        Voucher voucher = voucherMapper.toVoucher(null, null);
        assertNull(voucher);
    }

    @Test
    void testVoucherToVoucherDto_Gift_When_GladyUser_Are_Null(){
        Voucher voucher = ObjectHelper.getVoucherGift();
        voucher.getWallet().setGladyUser(null);
        VoucherDTO voucherDTO = voucherMapper.toVoucherDTO(voucher);

        assertNotNull(voucherDTO);
        assertEquals(voucher.getId(), voucherDTO.getId());
        assertEquals(voucher.getCode(), voucherDTO.getCode());
        assertEquals(voucher.getAmount(), voucherDTO.getAmount());
        assertEquals(voucher.getCreatedOn(), voucherDTO.getCreatedOn());
        assertEquals(voucher.getExpiresOn(), voucherDTO.getExpiresOn());
        assertEquals(voucher.getReceivedFrom(), voucherDTO.getReceivedFrom());
        assertEquals(voucher.getWallet().getId(), voucherDTO.getWalletId());
    }

}
