package com.glady.challenge.web.mapper;

import com.glady.challenge.model.company.Company;
import com.glady.challenge.web.dto.company.CompanyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDTO toCompanyDTO(Company company);

    Company toCompany(CompanyDTO companyDTO);

}
