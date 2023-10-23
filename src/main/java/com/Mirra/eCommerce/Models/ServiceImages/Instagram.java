package com.Mirra.eCommerce.Models.ServiceImages;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instagram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    @Lob
    @Column(columnDefinition = "LONGBLOB",length = Integer.MAX_VALUE)
    private byte[] image;
}
