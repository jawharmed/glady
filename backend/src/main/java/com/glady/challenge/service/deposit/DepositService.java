package com.glady.challenge.service.deposit;

import com.glady.challenge.exception.GladyException;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.deposit.Deposit;
import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.repository.DepositRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.service.gladyuser.GladyUserService;
import com.glady.challenge.service.wallet.VoucherService;
import com.glady.challenge.service.wallet.WalletService;
import com.glady.challenge.web.dto.deposit.DepositDTO;
import com.glady.challenge.web.dto.wallet.VoucherDTO;
import com.glady.challenge.web.dto.wallet.WalletDTO;
import com.glady.challenge.web.mapper.CompanyMapper;
import com.glady.challenge.web.mapper.DepositMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DepositService {

    private final DepositRepository depositRepository;
    private final CompanyService companyService;
    private final GladyUserService gladyUserService;
    private final WalletService walletService;
    private final VoucherService voucherService;


    /**
     * Make deposit
     * @param depositDTO deposit information
     * @return deposit dto
     * @throws GladyException if an error occurred
     */
    public DepositDTO makeDeposit(DepositDTO depositDTO) throws GladyException {
        Assert.isNull(depositDTO.getId(), "As ID is auto-generated, it must not be valued on creation.");
        Company company = this.companyService.getCompanyById(depositDTO.getCompanyId(), false);
        VoucherTypeEnum voucherTypeEnum = VoucherTypeEnum.valueOfType(depositDTO.getDepositType());
        Assert.notNull(voucherTypeEnum, "Voucher Type must be either 'Gift' or 'Meal'");

        // Check if company's balance allows this deposit
        if(!this.checkCompanyBalance(company, depositDTO.getAmount(), depositDTO.getDepositType())){
            throw new GladyException(String.format(ErrorMessage.COMPANY_BALANCE_INSUFFICIENT, company.getCompanyName()));
        }else{
            // Re-calculate company's balance
            if(voucherTypeEnum.equals(VoucherTypeEnum.GIFT)){
                company.setGiftBalance( company.getGiftBalance() - depositDTO.getAmount() );
            }else{
                company.setMealBalance(company.getMealBalance() - depositDTO.getAmount() );
            }
            this.companyService.update(CompanyMapper.INSTANCE.toCompanyDTO(company));
        }

        // Check if Glady user has a wallet, otherwise create one for him!
        GladyUser gladyUser = this.gladyUserService.getById(depositDTO.getGladyUserId());
        Wallet wallet = this.walletService.getOrCreate(WalletDTO.builder()
                            .walletType(voucherTypeEnum.getType())
                            .gladyUserId(gladyUser.getId())
                            .build());


        // Generate a voucher
        ZonedDateTime createdOn = ZonedDateTime.now();
        VoucherDTO voucherDTO = VoucherDTO.builder()
                .amount(depositDTO.getAmount())
                .createdOn(createdOn)
                .expiresOn(this.getExpireDate(createdOn, voucherTypeEnum))
                .receivedFrom(company.getCompanyName())
                .code(this.generateVoucherCode(voucherTypeEnum))
                .walletId(wallet.getId())
                .build();

        // Create voucher
        this.voucherService.create(voucherDTO, wallet);

        // Create a deposit
        Deposit deposit = DepositMapper.INSTANCE.toDeposit(depositDTO, gladyUser);
        this.depositRepository.save(deposit);

        return DepositMapper.INSTANCE.toDepositDTO(deposit);
    }

    /**
     * Check if the company has enought monney in his balance to generate the voucher with the given amount
     * @param company Comapny information
     * @param amount Amount of the deposit
     * @param voucherType Type of the voucher
     * @return True or False
     */
    private boolean checkCompanyBalance(Company company, double amount, String voucherType){
        if(VoucherTypeEnum.GIFT.equals(VoucherTypeEnum.valueOfType(voucherType))){
            return company.getGiftBalance() - amount >= 0;
        }else{
            return company.getMealBalance() - amount >= 0;
        }
    }

    /**
     * Calculate the expiration date of the voucher
     * @param createdOn Creation Date of the voucher (now)
     * @param voucherType voucher type
     * @return Expiration date
     */
    private ZonedDateTime getExpireDate(ZonedDateTime createdOn, VoucherTypeEnum voucherType){
        ZonedDateTime expiresOn;
        if(VoucherTypeEnum.GIFT.equals(voucherType)){
            expiresOn = createdOn.plusYears(1);
        }else{
            expiresOn = createdOn
                    .withYear(createdOn.getYear() + 1)
                    .withMonth(Month.FEBRUARY.getValue())
                    .with(TemporalAdjusters.lastDayOfMonth())
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59);
        }
        return expiresOn;
    }

    /**
     * Generate voucher code
     *
     * @param voucherType Gift or Meal
     * @return voucher code
     */
    private String generateVoucherCode(VoucherTypeEnum voucherType) {
        String code;
        if(VoucherTypeEnum.GIFT.equals(voucherType)){
            code = "G-" + Instant.now();
        }else{
            code = "M-" + Instant.now();
        }
        return code;
    }


}
