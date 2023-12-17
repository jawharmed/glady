package com.glady.challenge.web.mapper;

import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = CompanyMapper.class)
public interface GladyUserMapper {

    GladyUserMapper INSTANCE = Mappers.getMapper(GladyUserMapper.class);

    @Mapping(target = "companyId", source = "gladyUser.company.id")
    GladyUserDTO toGladyUserDTO(GladyUser gladyUser);

    @Mapping(target = "id", source = "gladyUserDTO.id")
    @Mapping(target = "company", source = "company")
    GladyUser toGladyUser(GladyUserDTO gladyUserDTO, Company company);

}
