package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Users.Payment;

public interface SelectPaymentService {
    public Payment selectPaymentMethod(String paymentMethod) ;
}
