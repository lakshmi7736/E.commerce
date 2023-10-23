package com.Mirra.eCommerce.Models.Users;

import com.Mirra.eCommerce.Models.Token.RefreshToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String mobileNo;

    @Column(unique = true)
    @NaturalId(mutable = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private String role;

    private boolean enable;

    private boolean accountNonLocked;

    private int failedAttempt;

    private LocalDateTime lockTime;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    @ToString.Exclude
    private RefreshToken refreshToken;

    private boolean adminDelete;

}