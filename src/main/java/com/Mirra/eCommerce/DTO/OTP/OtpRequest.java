package com.Mirra.eCommerce.DTO.OTP;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpRequest {
    private String username;
    private String phoneNumber;
}
