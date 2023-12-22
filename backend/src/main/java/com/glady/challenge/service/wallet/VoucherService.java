package com.glady.challenge.service.wallet;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.repository.VoucherRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.web.dto.wallet.VoucherDTO;
import com.glady.challenge.web.mapper.VoucherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class VoucherService {

    private final VoucherRepository voucherRepository;

    /**
     * Create new Voucher
     *
     * @param voucherDTO voucher information
     * @param wallet wallet information
     * @return new created voucher information
     * @throws EntityAlreadyExistException If voucher already exist
     */
    public Voucher create(VoucherDTO voucherDTO, Wallet wallet) {
        checkIfVoucherExist(voucherDTO.getCode());
        Voucher voucher = VoucherMapper.INSTANCE.toVoucher(voucherDTO, wallet);
        return voucherRepository.save(voucher);
    }

    /**
     * Get all voucher by wallet
     * @param walletId Wallet ID
     * @return List of vouchers
     */
    public List<Voucher> getVouchersByWallet(Long walletId){
        return voucherRepository.findAllByWalletId(walletId);
    }

    /**
     * Check if voucher code exist
     *
     * @param code voucher code value
     * @throws EntityAlreadyExistException If voucher already exist
     */
    private void checkIfVoucherExist(String code) throws EntityAlreadyExistException {
        Optional<Voucher> existingVoucher = voucherRepository.findByCodeIgnoreCase(code);
        if(existingVoucher.isPresent()){
            throw new EntityAlreadyExistException(String.format(ErrorMessage.VOUCHER_CODE_EXIST, code));
        }
    }


}
