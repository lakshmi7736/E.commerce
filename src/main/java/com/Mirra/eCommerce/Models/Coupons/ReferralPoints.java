package com.Mirra.eCommerce.Models.Coupons;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double referralBonus;

    private double activatedBonus;

}
