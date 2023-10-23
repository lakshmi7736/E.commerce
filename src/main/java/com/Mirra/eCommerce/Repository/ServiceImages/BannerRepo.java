package com.Mirra.eCommerce.Repository.ServiceImages;

import com.Mirra.eCommerce.Models.ServiceImages.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepo extends JpaRepository<Banner,Integer> {
    // Define a custom query to find any banner
    @Query("SELECT b FROM Banner b")
    Banner findAnyBanner();
}
