package com.glady.challenge.web.mapper;

import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.web.dto.wallet.WalletDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = GladyUser.class)
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(target = "gladyUserId", source = "wallet.gladyUser.id")
    @Mapping(target = "walletType", source = "walletType.type")
    WalletDTO toWalletDTO(Wallet wallet);

    @Mapping(target = "id", source = "walletDTO.id")
    @Mapping(target = "walletType", expression = "java(VoucherTypeEnum.valueOfType(walletDTO.getWalletType()))")
    @Mapping(target = "gladyUser", source = "gladyUser")
    Wallet toWallet(WalletDTO walletDTO, GladyUser gladyUser);

}
