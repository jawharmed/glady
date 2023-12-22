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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
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
     * Get an existing wallet or Create a new one if not
     * @param walletDTO Wallet information
     * @return Wallet information
     * @throws EntityAlreadyExistException If Wallet already exists
     */
    @Transactional
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


}
