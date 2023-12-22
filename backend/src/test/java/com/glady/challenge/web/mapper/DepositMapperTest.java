package com.glady.challenge.web.mapper;

import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.deposit.Deposit;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.web.dto.deposit.DepositDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DepositMapperTest {

    private final DepositMapper depositMapper = DepositMapper.INSTANCE;

    @Test
    void testDepositToDepositDto_Meal() {
        Deposit deposit = ObjectHelper.getDepositMeal();

        DepositDTO depositDTO = depositMapper.toDepositDTO(deposit);

        assertNotNull(depositDTO);
        assertEquals(deposit.getId(), depositDTO.getId());
        assertEquals(deposit.getAmount(), depositDTO.getAmount());
        assertEquals(deposit.getDepositType().getType(), depositDTO.getDepositType());
        assertEquals(deposit.getCompany().getId(), depositDTO.getCompanyId());
        assertEquals(deposit.getGladyUser().getId(), depositDTO.getGladyUserId());
    }

    @Test
    void testDepositDtoToDeposit_Meal() {
        DepositDTO depositDTO = DtoObjectHelper.getDepositMealDTO();
        GladyUser gladyUser = ObjectHelper.getGladyUser();

        Deposit deposit = depositMapper.toDeposit(depositDTO, gladyUser);

        assertNotNull(deposit);
        assertEquals(depositDTO.getId(), deposit.getId());
        assertEquals(depositDTO.getAmount(), deposit.getAmount());
        assertEquals(depositDTO.getDepositType(), deposit.getDepositType().getType());
        assertEquals(depositDTO.getCompanyId(), deposit.getCompany().getId());
        assertEquals(depositDTO.getGladyUserId(), deposit.getGladyUser().getId());
    }

    @Test
    void testDepositToDepositDto_Meal_When_GladyUser_And_Company_Are_Null() {
        Deposit deposit = ObjectHelper.getDepositMeal();
        deposit.setGladyUser(null);
        deposit.setCompany(null);

        DepositDTO depositDTO = depositMapper.toDepositDTO(deposit);

        assertNotNull(depositDTO);
        assertEquals(deposit.getId(), depositDTO.getId());
        assertEquals(deposit.getAmount(), depositDTO.getAmount());
        assertEquals(deposit.getDepositType().getType(), depositDTO.getDepositType());
        assertNull(depositDTO.getCompanyId());
        assertNull(depositDTO.getGladyUserId());
    }

    @Test
    void testDepositDtoToDeposit_Meal_When_GladyUser_And_Company_Are_Null() {
        DepositDTO depositDTO = DtoObjectHelper.getDepositMealDTO();
        depositDTO.setGladyUserId(null);
        depositDTO.setCompanyId(null);

        Deposit deposit = depositMapper.toDeposit(depositDTO, null);

        assertNotNull(deposit);
        assertEquals(depositDTO.getId(), deposit.getId());
        assertEquals(depositDTO.getAmount(), deposit.getAmount());
        assertEquals(depositDTO.getDepositType(), deposit.getDepositType().getType());
        assertNull(deposit.getCompany());
        assertNull(deposit.getGladyUser());
    }

    @Test
    void testDepositToDepositDto_Meal_When_Deposit_Is_Null() {
        DepositDTO depositDTO = depositMapper.toDepositDTO(null);
        assertNull(depositDTO);
    }

    @Test
    void testDepositDtoToDeposit_Meal_When_DepositDTO_Is_Null() {
        Deposit deposit = depositMapper.toDeposit(null, null);
        assertNull(deposit);
    }
}
