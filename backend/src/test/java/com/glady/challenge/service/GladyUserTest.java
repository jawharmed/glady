package com.glady.challenge.service;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.repository.GladyUserRepository;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.service.gladyuser.GladyUserService;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import com.glady.challenge.web.mapper.GladyUserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class GladyUserTest {

    @Mock
    private GladyUserRepository gladyUserRepository;

    @Mock
    private CompanyService companyService;

    @Spy
    @InjectMocks
    private GladyUserService gladyUserService;


    @Test
    void testGetById_GivenExistingId_ShouldReturnGladyUser() {
        Long userId = 1L;
        GladyUser gladyUser = new GladyUser();
        when(gladyUserRepository.findById(userId)).thenReturn(Optional.of(gladyUser));

        GladyUser result = gladyUserService.getById(userId);

        assertEquals(gladyUser, result);
        verify(gladyUserRepository, times(1)).findById(userId);
    }

    @Test
    void testGetById_GivenNonExistingId_ShouldThrowsEntityNotFoundException() {
        Long userId = 1L;
        when(gladyUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gladyUserService.getById(userId));

        verify(gladyUserRepository, times(1)).findById(userId);
    }

    @Test
    void testGetDtoById_GivenExistingId_ShouldReturnsGladyUserDTO() {
        GladyUser gladyUser = ObjectHelper.getGladyUser();
        when(gladyUserRepository.findById(1L)).thenReturn(Optional.of(gladyUser));
        GladyUserDTO result = gladyUserService.getDtoById(gladyUser.getId());

        assertNotNull(gladyUser);
        assertEquals(gladyUser.getId(), result.getId());
        assertEquals(gladyUser.getUsername(), result.getUsername());
        assertEquals(gladyUser.getCompany().getId(), result.getCompanyId());

        verify(gladyUserRepository, times(1)).findById(gladyUser.getId());
    }

    @Test
    void testGetDtoById_GivenNonExistingId_ShouldThrowsEntityNotFoundException() {
        Long userId = 1L;
        when(gladyUserRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> gladyUserService.getDtoById(userId));

        verify(gladyUserRepository, times(1)).findById(userId);
    }

    @Test
    void testCreate_GivenNonExistingUser_ShouldCreateAndReturnGladyUser() {
        GladyUserDTO gladyUserDTO = DtoObjectHelper.getGladyUserDTO();
        Company company = ObjectHelper.getCompany();

        when(companyService.getCompanyById(gladyUserDTO.getId(), false)).thenReturn(company);
        when(gladyUserRepository.save(any())).thenAnswer(response -> response.getArgument(0));

        GladyUserDTO result = gladyUserService.create(gladyUserDTO);

        assertNotNull(result);
        assertEquals(gladyUserDTO.getId(), result.getId());
        assertEquals(gladyUserDTO.getUsername(), result.getUsername());
        assertEquals(gladyUserDTO.getCompanyId(), result.getCompanyId());
    }


    @Test
    void testCreate_GivenAlreadyExistingUser_ShouldThrowEntityAlreadyExistException(){
        GladyUserDTO gladyUserDTO = DtoObjectHelper.getGladyUserDTO();
        when(gladyUserRepository.findByUsernameIgnoreCase(gladyUserDTO.getUsername())).thenReturn(Optional.of(new GladyUser()));
        verify(companyService, never()).getCompanyById(anyLong(), anyBoolean());
        verify(gladyUserRepository, never()).save(any());
        assertThrows(EntityAlreadyExistException.class, () -> gladyUserService.create(gladyUserDTO));
    }

    @Test
    void testUpdate_GivenSameExistingUsername_ShouldUpdateAndReturnGladyUser(){
        GladyUserDTO existingGladyUserDTO = DtoObjectHelper.getGladyUserDTO();
        Company company = new Company(existingGladyUserDTO.getCompanyId(), "Company", 10, 20, null);
        GladyUser gladyUser = new GladyUser(existingGladyUserDTO.getId(), existingGladyUserDTO.getUsername(), company, new ArrayList<>());

        when(gladyUserRepository.findById(existingGladyUserDTO.getId())).thenReturn(Optional.of(gladyUser));
        when(companyService.getCompanyById(existingGladyUserDTO.getId(), false)).thenReturn(company);
        when(gladyUserRepository.save(any())).thenAnswer(reponse -> reponse.getArgument(0));

        GladyUserDTO result = gladyUserService.update(existingGladyUserDTO);

        assertNotNull(result);
        assertEquals(existingGladyUserDTO.getId(), result.getId());
        assertEquals(existingGladyUserDTO.getCompanyId(), result.getCompanyId());
    }

    @Test
    void testUpdate_GivenDifferentNonExistingUsername_ShouldThrowEntityAlreadyExistException(){
        GladyUserDTO existingGladyUserDTO = DtoObjectHelper.getGladyUserDTO();
        GladyUser gladyUser = new GladyUser(2L, "Another Username", new Company(), new ArrayList<>());

        when(gladyUserRepository.findById(existingGladyUserDTO.getId())).thenReturn(Optional.of(gladyUser));
        when(gladyUserRepository.findByUsernameIgnoreCase(existingGladyUserDTO.getUsername())).thenReturn(Optional.of(new GladyUser()));

        assertThrows(EntityAlreadyExistException.class, () -> gladyUserService.update(existingGladyUserDTO));
        verify(companyService, never()).getCompanyById(anyLong(), anyBoolean());
        verify(gladyUserRepository, never()).save(any());
    }

    @Test
    void testUpdate_GivenNonExistingGladyUser_ShouldThrowEntityNotFoundException(){
        GladyUserDTO existingGladyUserDTO = DtoObjectHelper.getGladyUserDTO();
        when(gladyUserRepository.findById(existingGladyUserDTO.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gladyUserService.update(existingGladyUserDTO));
        verify(companyService, never()).getCompanyById(anyLong(), anyBoolean());
        verify(gladyUserRepository, never()).save(any());
    }

    @Test
    void testDelete_GivenExistingGladyUser_ShouldDeleteUser(){
        Long gladyUserId = 1L;
        when(gladyUserRepository.findById(gladyUserId)).thenReturn(Optional.of(new GladyUser()));
        gladyUserService.delete(gladyUserId);
        verify(gladyUserRepository, times(1)).deleteById(gladyUserId);
    }

    @Test
    void testDelete_GivenNonExistingGladyUser_ShouldThrowEntityNotFoundException(){
        Long gladyUserId = 1L;
        when(gladyUserRepository.findById(gladyUserId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gladyUserService.delete(gladyUserId));

        verify(gladyUserRepository, never()).deleteById(gladyUserId);
        verify(gladyUserRepository, times(1)).findById(gladyUserId);
    }
}
