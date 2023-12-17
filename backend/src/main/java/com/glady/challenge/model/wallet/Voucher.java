package com.glady.challenge.model.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VOUCHER")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AMOUNT", precision = 2)
    private double amount;

    @Column(name = "CREATED_ON")
    @CreatedDate
    private ZonedDateTime createdOn;

    @Column(name = "EXPIRES_ON")
    private ZonedDateTime expiresOn;

    @Column(name = "RECEIVED_FROM")
    private String receivedFrom;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "WALLET_ID", nullable = false, referencedColumnName = "id")
    private Wallet wallet;
}
