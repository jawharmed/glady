package com.glady.challenge.model.wallet;

import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.user.GladyUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WALLET")
public class Wallet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "WALLET_TYPE")
    private VoucherTypeEnum walletType;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private GladyUser gladyUser;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Wallet wallet = (Wallet) object;
        return walletType == wallet.walletType && Objects.equals(gladyUser, wallet.gladyUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletType, gladyUser);
    }
}
