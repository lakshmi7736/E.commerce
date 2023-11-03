package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Users.Payment;
import org.springframework.stereotype.Service;

@Service
public class SelectPaymentServiceImpl implements SelectPaymentService{
    @Override
    public Payment selectPaymentMethod(String paymentMethod) {
        switch (paymentMethod) {
            case "COD":
                return Payment.COD;
            case "UPI":
                return Payment.UPI;
            case "WALLET":
                return Payment.WALLET;
            // Handle other payment methods or invalid input if needed
            default:
                throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
    }
}
