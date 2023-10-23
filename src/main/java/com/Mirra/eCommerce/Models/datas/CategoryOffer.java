package com.Mirra.eCommerce.Models.datas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    private String offerTitle;




}
