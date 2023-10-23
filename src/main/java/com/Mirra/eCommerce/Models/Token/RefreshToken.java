package com.Mirra.eCommerce.Models.Token;


import com.Mirra.eCommerce.Models.Users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;

    @Column(unique = true)
    private String refreshToken;

    private Instant expiry;

    @OneToOne
    private User user;
}
