package com.glady.challenge.model.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPANY")
@SQLDelete(sql = "UPDATE COMPANY SET DELETED_ON = true WHERE id=?")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPANY_NAME", nullable = false, unique = true)
    private String companyName;

    @Column(name = "MEAL_BALANCE", precision = 2)
    private double mealBalance;

    @Column(name = "GIFT_BALANCE", precision = 2)
    private double giftBalance;

    @Column(name = "DELETED_ON")
    private ZonedDateTime deletedOn;
}
