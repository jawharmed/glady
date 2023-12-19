package com.glady.challenge.repository;

import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByGladyUserIdAndWalletType(Long gladyUserId, VoucherTypeEnum voucherType);
}
