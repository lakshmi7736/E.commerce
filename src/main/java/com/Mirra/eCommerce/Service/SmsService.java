package com.Mirra.eCommerce.Service;


import com.Mirra.eCommerce.Configure.TwilioConfig;
import com.Mirra.eCommerce.DTO.OTP.OtpRequest;
import com.Mirra.eCommerce.DTO.OTP.OtpResponseDto;
import com.Mirra.eCommerce.DTO.OTP.OtpStatus;
import com.Mirra.eCommerce.DTO.OTP.OtpValidationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

//@Service
//@Slf4j
//public class SmsService {
//
//    @Autowired
//    private TwilioConfig twilioConfig;
//    Map<String, String> otpMap = new HashMap<>();
//
//
//    public OtpResponseDto sendSMS(OtpRequest otpRequest) {
//        OtpResponseDto otpResponseDto = null;
//        try {
//            PhoneNumber to = new PhoneNumber("+91"+otpRequest.getPhoneNumber());//to
//            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from
//            String otp = generateOTP();
//            String otpMessage = "Dear Customer , Your OTP is  " + otp + " for sending sms through Spring boot application. Thank You.";
//            Message message = Message
//                    .creator(to, from,
//                            otpMessage)
//                    .create();
//            otpMap.put(otpRequest.getUsername(), otp);
//            otpMap.keySet();
//            System.out.println("map details "+otpMap);
//            otpResponseDto = new OtpResponseDto(OtpStatus.DELIVERED, otpMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//            otpResponseDto = new OtpResponseDto(OtpStatus.FAILED, e.getMessage());
//        }
//        return otpResponseDto;
//    }
//
//
//    public String validateOtp(OtpValidationRequest otpValidationRequest) {
//        System.out.println("otp " + otpValidationRequest.getOtpNumber() + " " + "username " + otpValidationRequest.getUsername());
//        System.out.println("map details in validate  "+otpMap);
//
//        Set<String> keys = otpMap.keySet();
//        System.out.println("keys " + keys);
//
//        String username = null;
//        if (keys.contains(otpValidationRequest.getUsername())) {
//            username=otpValidationRequest.getUsername();
//            System.out.println("Username exists in the set of keys");
//        } else {
//            System.out.println("Username does not exist in the set of keys");
//        }
//
//        System.out.println("keyName " + username);
//
//        if (username != null) {
//            otpMap.remove(username); // Remove the entry for the validated username
//            return "OTP is valid!";
//        } else {
//            return "OTP is invalid!";
//        }
//    }
//
//    private String generateOTP() {
//        return new DecimalFormat("000000")
//                .format(new Random().nextInt(999999));
//    }
//
//}

@Service
@Slf4j
public class SmsService {

    @Autowired
    private TwilioConfig twilioConfig;
    Map<String, String> otpMap = new HashMap<>();


    public boolean sendSMS(OtpRequest otpRequest) {
        OtpResponseDto otpResponseDto = null;
        System.out.println("INSIDE POST");
        try { PhoneNumber to = new PhoneNumber("+91"+otpRequest.getPhoneNumber());//to
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber()); // from
            String otp = generateOTP();
            String otpMessage = "Dear Customer , Your OTP is  " + otp + " for sending sms through Spring boot application. Thank You.";
            Message message = Message
                    .creator(to, from,
                            otpMessage)
                    .create();
            otpMap.put(otpRequest.getUsername(), otp);
            otpResponseDto = new OtpResponseDto(OtpStatus.DELIVERED, otpMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            otpResponseDto = new OtpResponseDto(OtpStatus.FAILED, e.getMessage());
            return false;
        }
    }


    public boolean validateOtp(OtpValidationRequest otpValidationRequest) {
        String otp = otpMap.get(otpValidationRequest.getUsername());
        if (otp != null && otp.equals(otpValidationRequest.getOtpNumber())) {
            otpMap.remove(otpValidationRequest.getUsername());
            System.out.println("valid");
            return true;
        } else {
            System.out.println("invalid");
            return false;
        }
    }

    private String generateOTP() {
        return new DecimalFormat("00000")
                .format(new Random().nextInt(99999));
    }

}