package com.glady.challenge.service;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.repository.GladyUserRepository;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.service.gladyuser.GladyUserService;
import com.glady.challenge.service.wallet.VoucherService;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import com.glady.challenge.web.dto.user.GladyUserInfoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Mock
    private VoucherService voucherService;

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


    @Test
    void testGetGladyUserInfo() {
        GladyUser gladyUser = ObjectHelper.getGladyUser();

        Company company = ObjectHelper.getCompany();
        gladyUser.setCompany(company);

        List<Wallet> wallets = new ArrayList<>();
        Wallet giftWallet = ObjectHelper.getWalletGift();
        Wallet mealWallet = ObjectHelper.getWalletMeal();
        wallets.add(giftWallet);
        wallets.add(mealWallet);
        gladyUser.setWallets(wallets);

        Map<VoucherTypeEnum, List<Voucher>> vouchersMap = new HashMap<>();
        vouchersMap.put(VoucherTypeEnum.GIFT, Collections.singletonList(ObjectHelper.getVoucherGift()));
        vouchersMap.put(VoucherTypeEnum.MEAL, Collections.singletonList(ObjectHelper.getVoucherMeal()));

        when(gladyUserRepository.findById(gladyUser.getId())).thenReturn(Optional.of(gladyUser));

        when(voucherService.getVouchersByWallet(giftWallet.getId())).thenReturn(vouchersMap.get(VoucherTypeEnum.GIFT));
        when(voucherService.getVouchersByWallet(mealWallet.getId())).thenReturn(vouchersMap.get(VoucherTypeEnum.MEAL));

        GladyUserInfoDTO result = gladyUserService.getGladyUserInfo(gladyUser.getId());

        assertEquals(gladyUser.getId(), result.getId());
        assertEquals(gladyUser.getUsername(), result.getUsername());
        assertEquals(company.getCompanyName(), result.getCompanyName());
        assertEquals(1, result.getValideGiftVouchersCount());
        assertEquals(125, result.getGiftBalance());
        assertEquals(1, result.getValideMealVouchersCount());
        assertEquals(200, result.getMealBalance());
        assertEquals(0, result.getTotalExpiredVouchersCount());

        verify(voucherService, times(wallets.size())).getVouchersByWallet(anyLong());
    }


    @Test
    void testGetAll_GivenEmptyTable_ShouldReturnEmptyPage() {
        when(gladyUserRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Page<GladyUserDTO> result = gladyUserService.getAll(0, 10, "asc");
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testGetAll_GivenNonEmptyTable_ShouldReturnPageOfGladyUserDTO() {
        List<GladyUser> gladyUserList = Collections.singletonList(ObjectHelper.getGladyUser());

        Page<GladyUser> gladyUserPage = new PageImpl<>(gladyUserList);
        when(gladyUserRepository.findAll(any(Pageable.class))).thenReturn(gladyUserPage);

        Page<GladyUserDTO> result = gladyUserService.getAll(0, 10, "asc");

        assertEquals(gladyUserList.size(), result.getTotalElements());
    }
}
