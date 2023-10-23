package com.Mirra.eCommerce.Repository.ServiceImages;

import com.Mirra.eCommerce.Models.ServiceImages.Instagram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstagramRepo extends JpaRepository<Instagram, Integer> {
}
