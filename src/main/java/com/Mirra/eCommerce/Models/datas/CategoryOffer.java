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
public class CategoryOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(columnDefinition = "LONGBLOB",length = Integer.MAX_VALUE)
    private byte[] image;

    private String saleEvent;

    private BigDecimal discountPrice;

    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //to view the image Base64-Encoder string value is returned.
    public String getImageDataAsBase64(){
        if(image!=null&&image.length>0){
            return Base64.getEncoder().encodeToString(image);
        }
        return null;
    }



}
