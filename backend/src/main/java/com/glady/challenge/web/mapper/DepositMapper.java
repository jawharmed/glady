package com.glady.challenge.web.mapper;

import com.glady.challenge.model.deposit.Deposit;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.web.dto.deposit.DepositDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepositMapper {

    DepositMapper INSTANCE = Mappers.getMapper(DepositMapper.class);

    @Mapping(target = "depositType", source = "depositType.type")
    @Mapping(target = "gladyUserId", source = "deposit.gladyUser.id")
    @Mapping(target = "companyId", source = "deposit.company.id")
    DepositDTO toDepositDTO(Deposit deposit);

    @Mapping(target = "id", source = "depositDTO.id")
    @Mapping(target = "depositType", expression = "java(VoucherTypeEnum.valueOfType(depositDTO.getDepositType()))")
    @Mapping(target = "gladyUser", source = "gladyUser")
    @Mapping(target = "company", source = "gladyUser.company")
    Deposit toDeposit(DepositDTO depositDTO, GladyUser gladyUser);

}
