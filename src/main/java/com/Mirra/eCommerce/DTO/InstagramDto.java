package com.Mirra.eCommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstagramDto {

    private int id;

    private String imageData;;

    private String instagramId;

    private String url;
}
