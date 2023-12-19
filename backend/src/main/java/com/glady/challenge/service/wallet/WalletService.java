package com.glady.challenge.service.wallet;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Wallet;
import com.glady.challenge.repository.WalletRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.service.gladyuser.GladyUserService;
import com.glady.challenge.web.dto.wallet.WalletDTO;
import com.glady.challenge.web.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private static final String ENTITY_NAME = "Wallet";
    private static final WalletMapper walletMapper = WalletMapper.INSTANCE;

    private final WalletRepository walletRepository;
    private final GladyUserService gladyUserService;

    /**
     * Get Wallet information
     * @param walletId Wallet ID
     * @return Wallet DTO information
     */
    public WalletDTO getDtoById(Long walletId){
        return walletMapper.toWalletDTO( getById(walletId));
    }

    /**
     * Get Wallet information
     * @param walletId Wallet ID
     * @return Wallet information
     */
    public Wallet getById(Long walletId) {
        return this.walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessage.ENTITY_ID_NOT_FOUND, ENTITY_NAME, walletId)));
    }

    /**
     * Get user wallet information if exist
     *
     * @param gladyUserId Glady User ID
     * @param voucherType Voucher Type
     * @return Wallet DTO information
     */
    public WalletDTO getbyGladyUserIdAndVoucherType(Long gladyUserId, VoucherTypeEnum voucherType){
        Wallet wallet = this.walletRepository.findByGladyUserIdAndWalletType(gladyUserId, voucherType).orElse(null);
        return wallet != null ? walletMapper.toWalletDTO(wallet) : null;
    }

    /**
     * Create a new Wallet
     * @param walletDTO Wallet information
     * @return Wallet information
     * @throws EntityAlreadyExistException If Wallet already exists
     */
    public WalletDTO create(WalletDTO walletDTO) throws EntityAlreadyExistException {
        checkIfWalletExist(walletDTO);
        GladyUser gladyUser = gladyUserService.getById(walletDTO.getGladyUserId());
        Wallet wallet = walletRepository.save(walletMapper.toWallet(walletDTO, gladyUser));
        return walletMapper.toWalletDTO(wallet);
    }

    public Wallet getOrCreate(WalletDTO walletDTO){
        Optional<Wallet> existingWallet = this.walletRepository.findByGladyUserIdAndWalletType(
                walletDTO.getGladyUserId(),
                VoucherTypeEnum.valueOfType(walletDTO.getWalletType()));

        Wallet wallet;
        if(existingWallet.isPresent()){
            wallet = existingWallet.get();
        }else{
            GladyUser gladyUser = gladyUserService.getById(walletDTO.getGladyUserId());
            wallet = walletRepository.save(walletMapper.toWallet(walletDTO, gladyUser));
        }

        return wallet;
    }


    private void checkIfWalletExist(WalletDTO walletDTO) throws EntityAlreadyExistException {
        Optional<Wallet> existingWallet = walletRepository.findById(walletDTO.getId());
        if(existingWallet.isPresent()){
            throw new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_ID_ALREADY_EXIST, ENTITY_NAME, walletDTO.getId()));
        }
    }


}
