package com.Mirra.eCommerce.Models.Token;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class JwtResponse {
    private String jwtToken;
    private String refreshToken;
    private String username;
    private String name;
}
