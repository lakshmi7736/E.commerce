package com.Mirra.eCommerce.Models.datas;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SubCategory {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category parentCategory;

}
