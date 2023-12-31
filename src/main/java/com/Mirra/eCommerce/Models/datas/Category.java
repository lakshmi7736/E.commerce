package com.Mirra.eCommerce.Models.datas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(length = 225)
    private String description;


    private boolean active;

    @OneToOne(mappedBy = "category")
    private CategoryOffer categoryOffer;


    @Lob
    @Column(columnDefinition = "LONGBLOB",length = Integer.MAX_VALUE)
    private byte[] image;


    //to view the image Base64-Encoder string value is returned.
    public String getImageDataAsBase64(){
        if(image!=null&&image.length>0){
            return Base64.getEncoder().encodeToString(image);
        }
        return null;
    }

    public void checkExpirationDate() {
        LocalDate currentDate = LocalDate.now();
        if (categoryOffer.getExpirationDate() != null && currentDate.isAfter(categoryOffer.getExpirationDate())) {
            // If the expiration date is in the past, set discountPrice to 0
            categoryOffer.setDiscountPrice(null);
        }
    }


}