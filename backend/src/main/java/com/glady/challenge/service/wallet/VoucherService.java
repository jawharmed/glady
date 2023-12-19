package com.glady.challenge.service.wallet;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.repository.VoucherRepository;
import com.glady.challenge.service.common.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {

//    private static final String ENTITY_NAME = "Voucher";
//    private static final VoucherMapper voucherMapper = VoucherMapper.INSTANCE;

    private final VoucherRepository voucherRepository;
//    private final WalletService walletService;

//    /**
//     * Create new Voucher
//     *
//     * @param voucherDTO voucher information
//     * @return new created voucher information
//     * @throws EntityAlreadyExistException If voucher already exist
//     */
////    public VoucherDTO create(VoucherDTO voucherDTO) throws EntityAlreadyExistException {
////        checkIfVoucherExist(voucherDTO);
////        Wallet wallet = this.walletService.getById(voucherDTO.getWalletId());
////        Voucher voucher = voucherRepository.save(voucherMapper.toVoucher(voucherDTO, wallet));
////        return voucherMapper.toVoucherDTO(voucher);
////    }

    /**
     * Create new Voucher
     *
     * @param voucher voucher information
     * @return new created voucher information
     * @throws EntityAlreadyExistException If voucher already exist
     */
    public Voucher create(Voucher voucher) {
        checkIfVoucherExist(voucher.getCode());
        return voucherRepository.save(voucher);
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
