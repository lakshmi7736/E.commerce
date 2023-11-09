package com.Mirra.eCommerce.DTO.OTP;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpValidationRequest {
    private String username;
    private String otpNumber;
    private String phoneNumber;
}