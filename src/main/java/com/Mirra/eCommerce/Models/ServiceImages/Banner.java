package com.Mirra.eCommerce.Models.ServiceImages;

import com.Mirra.eCommerce.Models.datas.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private String collection;

    private String saleEvent;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


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

}
