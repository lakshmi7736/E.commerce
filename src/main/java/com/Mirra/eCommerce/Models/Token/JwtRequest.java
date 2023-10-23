package com.Mirra.eCommerce.Models.Token;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtRequest {
   private String email;
   private String password;
}
