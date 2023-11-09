package com.Mirra.eCommerce.Models.Users.Related;

import com.Mirra.eCommerce.Models.Users.User;
import com.Mirra.eCommerce.Models.datas.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product products;

    private int quantity;

    private double total; // Added field to store the calculated total

    private BigDecimal discountPrice;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

