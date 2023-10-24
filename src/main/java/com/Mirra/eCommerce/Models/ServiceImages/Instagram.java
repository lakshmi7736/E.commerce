package com.Mirra.eCommerce.Models.ServiceImages;

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
public class Instagram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    @Column(columnDefinition = "LONGBLOB",length = Integer.MAX_VALUE)
    private byte[] image;

    private String instagramId;

    private String url;

    //to view the image Base64-Encoder string value is returned.
    public String getImageDataAsBase64(){
        System.out.println("inside method");
        if(image!=null&&image.length>0){
            return Base64.getEncoder().encodeToString(image);
        }
        return null;
    }
}
