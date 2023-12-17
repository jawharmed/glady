package com.glady.challenge.web.mapper;

import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.web.dto.wallet.VoucherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VoucherMapper {

    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);

    @Mapping(target = "walletId", source = "wallet.id")
    VoucherDTO toVoucherDTO(Voucher voucher);

    @Mapping(target = "id", source = "voucherDTO.id")
    Voucher toVoucher(VoucherDTO voucherDTO, Wallet wallet);


}
