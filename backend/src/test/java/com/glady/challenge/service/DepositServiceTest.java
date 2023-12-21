package com.glady.challenge.service;

import com.glady.challenge.exception.GladyException;
import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.deposit.Deposit;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.repository.DepositRepository;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.service.deposit.DepositService;
import com.glady.challenge.service.gladyuser.GladyUserService;
import com.glady.challenge.service.wallet.VoucherService;
import com.glady.challenge.service.wallet.WalletService;
import com.glady.challenge.web.dto.deposit.DepositDTO;
import com.glady.challenge.web.dto.wallet.VoucherDTO;
import com.glady.challenge.web.dto.wallet.WalletDTO;
import com.glady.challenge.web.mapper.VoucherMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @Mock
    private DepositRepository depositRepository;
    @Mock
    private CompanyService companyService;
    @Mock
    private GladyUserService gladyUserService;
    @Mock
    private WalletService walletService;
    @Mock
    private VoucherService voucherService;
    @Spy
    @InjectMocks
    private DepositService depositService;

    @Test
    void givenFixedClock_WhenNow_ShouldGetFixedInstant()  {
        String instantExpected = "2023-12-20T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));

        Instant instant = Instant.now(clock);
        assertEquals(instant.toString(), instantExpected);
    }


    private static final String INSTANT_EXPECTED = "2023-12-20T10:15:30Z";
    private static final String VOUCHER_GIFT_PREFIX = "G-";
    private static final String VOUCHER_MEAL_PREFIX = "M-";

    private Company givenCompany;
    private GladyUser givenGladyUser;
    private Wallet givenWallet;
    private DepositDTO givenDepositDto;

    Instant instant;
    ZonedDateTime createdOn;

    Voucher voucher;

    @BeforeEach
    void setUpMealDeposit() {
        Clock clock = Clock.fixed(Instant.parse(INSTANT_EXPECTED), ZoneId.of("UTC"));
        instant = Instant.now(clock);
        createdOn = ZonedDateTime.now(clock);

        givenCompany = ObjectHelper.getCompany();
        givenGladyUser = ObjectHelper.getGladyUser();
        givenWallet = ObjectHelper.getWalletMeal();

        givenGladyUser.setWallets(Collections.singletonList(givenWallet));

        givenDepositDto = DtoObjectHelper.getDepositMealDTO();
        givenDepositDto.setCompanyId(givenCompany.getId());
        givenDepositDto.setGladyUserId(givenGladyUser.getId());

        String giftVoucherCode = VOUCHER_MEAL_PREFIX + instant;
        VoucherDTO givenMealVoucherDto = createMealVoucherDTO(givenDepositDto, createdOn, giftVoucherCode);
        voucher = VoucherMapper.INSTANCE.toVoucher(givenMealVoucherDto, givenWallet);

    }

    @BeforeEach
    void setUpGiftDeposit() {
        Clock clock = Clock.fixed(Instant.parse(INSTANT_EXPECTED), ZoneId.of("UTC"));
        instant = Instant.now(clock);
        createdOn = ZonedDateTime.now(clock);

        givenCompany = ObjectHelper.getCompany();
        givenGladyUser = ObjectHelper.getGladyUser();
        givenWallet = ObjectHelper.getWalletGift();

        givenGladyUser.setWallets(Collections.singletonList(givenWallet));

        givenDepositDto = DtoObjectHelper.getDepositGiftDTO();
        givenDepositDto.setCompanyId(givenCompany.getId());
        givenDepositDto.setGladyUserId(givenGladyUser.getId());

        String giftVoucherCode = VOUCHER_GIFT_PREFIX + instant;
        VoucherDTO givenGiftVoucherDto = createGiftVoucherDTO(givenDepositDto, createdOn, giftVoucherCode);
        voucher = VoucherMapper.INSTANCE.toVoucher(givenGiftVoucherDto, givenWallet);

    }

    private VoucherDTO createGiftVoucherDTO(DepositDTO depositDTO, ZonedDateTime createdOn, String voucherCode) {
        return VoucherDTO.builder()
                .amount(depositDTO.getAmount())
                .receivedFrom(givenCompany.getCompanyName())
                .walletId(givenWallet.getId())
                .code(voucherCode)
                .createdOn(createdOn)
                .expiresOn(createdOn.plusYears(1))
                .build();
    }

    private VoucherDTO createMealVoucherDTO(DepositDTO depositDTO, ZonedDateTime createdOn, String voucherCode) {
        ZonedDateTime expiresOn = createdOn
                .withYear(createdOn.getYear() + 1)
                .withMonth(Month.FEBRUARY.getValue())
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        return VoucherDTO.builder()
                .amount(depositDTO.getAmount())
                .receivedFrom(givenCompany.getCompanyName())
                .walletId(givenWallet.getId())
                .code(voucherCode)
                .createdOn(createdOn)
                .expiresOn(expiresOn)
                .build();
    }

    @Test
    void testMakeDeposit_GivenGiftDeposit_ShouldCreateDepositAndGiftVoucher() throws GladyException {
        setUpGiftDeposit();

        when(companyService.getCompanyById(anyLong(), anyBoolean())).thenReturn(givenCompany);
        when(gladyUserService.getById(anyLong())).thenReturn(givenGladyUser);
        when(walletService.getOrCreate(any(WalletDTO.class))).thenReturn(givenWallet);
        when(voucherService.create(any(VoucherDTO.class), any(Wallet.class))).thenReturn(voucher);
        when(depositRepository.save(any(Deposit.class))).thenAnswer(response -> response.getArgument(0));

        DepositDTO result = depositService.makeDeposit(givenDepositDto);

        assertNotNull(result);
        assertEquals(givenDepositDto.getAmount(), result.getAmount());
        assertEquals(givenDepositDto.getGladyUserId(), result.getGladyUserId());
        assertEquals(givenDepositDto.getCompanyId(), result.getCompanyId());
        assertEquals(givenDepositDto.getDepositType(), result.getDepositType());
    }

    @Test
    void testMakeDeposit_GivenMealDeposit_ShouldCreateDepositAndMealVoucher() throws GladyException {
        setUpMealDeposit();

        when(companyService.getCompanyById(anyLong(), anyBoolean())).thenReturn(givenCompany);
        when(gladyUserService.getById(anyLong())).thenReturn(givenGladyUser);
        when(walletService.getOrCreate(any(WalletDTO.class))).thenReturn(givenWallet);
        when(voucherService.create(any(VoucherDTO.class), any(Wallet.class))).thenReturn(voucher);
        when(depositRepository.save(any(Deposit.class))).thenAnswer(response -> response.getArgument(0));

        DepositDTO result = depositService.makeDeposit(givenDepositDto);

        assertNotNull(result);
        assertEquals(givenDepositDto.getAmount(), result.getAmount());
        assertEquals(givenDepositDto.getGladyUserId(), result.getGladyUserId());
        assertEquals(givenDepositDto.getCompanyId(), result.getCompanyId());
        assertEquals(givenDepositDto.getDepositType(), result.getDepositType());
    }

    @Test
    void testMakeDeposit_GivenMealDeposit_WhenCompanyBalanceInsufficient_ShouldThrowGladyException() throws GladyException {
        setUpMealDeposit();
        givenCompany.setMealBalance(10);
        when(companyService.getCompanyById(anyLong(), anyBoolean())).thenReturn(givenCompany);
        assertThrows(GladyException.class, () -> depositService.makeDeposit(givenDepositDto));
    }

}
