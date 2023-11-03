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

//    public BigDecimal calculateGrandTotal(List<AddToCart> cartList) {
//        double grandTotal = 0.0;
//        for (AddToCart cartItem : cartList) {
//            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
//            BigDecimal actualPrice = cartItem.getProducts().getMyPrice();
//            BigDecimal total = actualPrice.multiply(quantity);
//            cartItem.setTotal(total.doubleValue());
//            grandTotal += total.doubleValue();
//        }
//        return grandTotal;
//    }

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

//    public BigDecimal calculateActualTotal(List<AddToCart> cartList) {
//        double subTotal = 0.0;
//        for (AddToCart cartItem : cartList) {
//            BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
//            BigDecimal actualPrice = cartItem.getProducts().getActualPrice();
//            BigDecimal total = actualPrice.multiply(quantity);
//            cartItem.setTotal(total.doubleValue());
//            subTotal += total.doubleValue();
//        }
//        return subTotal;
//    }



}
