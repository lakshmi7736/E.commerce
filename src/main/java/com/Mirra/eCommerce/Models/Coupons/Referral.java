package com.Mirra.eCommerce.Models.Coupons;

import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "referralPoints_id")
    private ReferralPoints referralPoints;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
