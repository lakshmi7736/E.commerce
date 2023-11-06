package com.Mirra.eCommerce.Service.Checkout;

import com.Mirra.eCommerce.Models.Users.Related.AddToCart;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CalculationServiceImpl implements CalculationService{
    @Override
    public BigDecimal calculateGrandTotal(List<AddToCart> cartList) {
        BigDecimal grandTotal = BigDecimal.ZERO;
        for (AddToCart cartItem : cartList) {
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
            BigDecimal actualPrice = cartItem.getProducts().getMyPrice();
            BigDecimal total = actualPrice.multiply(quantity);
            cartItem.setTotal(total.doubleValue());
            grandTotal = grandTotal.add(total);
        }
        return grandTotal;
    }

    @Override
    public BigDecimal calculateGrandTotal(AddToCart cart) {
        BigDecimal grandTotal = BigDecimal.ZERO;
            BigDecimal quantity = BigDecimal.valueOf(cart.getQuantity());
            BigDecimal actualPrice = cart.getProducts().getMyPrice();
            BigDecimal total = actualPrice.multiply(quantity);
            cart.setTotal(total.doubleValue());
            grandTotal = grandTotal.add(total);
        return grandTotal;
    }


    @Override
    public BigDecimal calculateActualTotal(List<AddToCart> cartList) {
        BigDecimal subTotal = BigDecimal.ZERO;
        for (AddToCart cartItem : cartList) {
            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
            BigDecimal actualPrice = cartItem.getProducts().getActualPrice();
            BigDecimal total = actualPrice.multiply(quantity);
            cartItem.setTotal(total.doubleValue());
            subTotal = subTotal.add(total);
        }
        return subTotal;
    }




}
