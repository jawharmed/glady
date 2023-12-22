package com.glady.challenge.web.mapper;


import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GladyUserMapperTest {

    private final GladyUserMapper gladyUserMapper = GladyUserMapper.INSTANCE;

    @Test
    void testGladyUserToGladyUserDto(){
        GladyUserDTO gladyUserDTO = DtoObjectHelper.getGladyUserDTO();
        Company company = ObjectHelper.getCompany();
        gladyUserDTO.setCompanyId(company.getId());

        GladyUser gladyUser = gladyUserMapper.toGladyUser(gladyUserDTO, company);

        assertNotNull(gladyUserDTO);
        assertEquals(gladyUserDTO.getId(), gladyUser.getId());
        assertEquals(gladyUserDTO.getUsername(), gladyUser.getUsername());
        assertEquals(gladyUserDTO.getCompanyId(), gladyUser.getCompany().getId());

    }

    @Test
    void testGladyUserDtoToGladyUser(){
        GladyUser gladyUser = ObjectHelper.getGladyUser();

        GladyUserDTO gladyUserDTO = gladyUserMapper.toGladyUserDTO(gladyUser);

        assertNotNull(gladyUser);
        assertEquals(gladyUser.getId(), gladyUserDTO.getId());
        assertEquals(gladyUser.getUsername(), gladyUserDTO.getUsername());
        assertEquals(gladyUser.getCompany().getId(), gladyUserDTO.getCompanyId());

    }

    @Test
    void testGladyUserToGladyUserDto_When_GladyUserDto_Is_Null(){
        GladyUser gladyUser = gladyUserMapper.toGladyUser(null, null);
        assertNull(gladyUser);
    }

    @Test
    void testGladyUserToGladyUserDto_When_GladyUser_Is_Null(){
        GladyUserDTO gladyUserDTO = gladyUserMapper.toGladyUserDTO(null);
        assertNull(gladyUserDTO);
    }
}
