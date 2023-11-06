package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Users.Related.AddToCart;

import java.math.BigDecimal;
import java.util.List;

public interface CalculationService {
    public BigDecimal calculateGrandTotal(List<AddToCart> cartList);

    public BigDecimal calculateGrandTotal(AddToCart cart);

    public BigDecimal calculateActualTotal(List<AddToCart> cartList) ;

}
