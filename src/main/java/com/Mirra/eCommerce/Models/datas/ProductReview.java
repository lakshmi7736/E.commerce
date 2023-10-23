package com.Mirra.eCommerce.Models.datas;

import com.Mirra.eCommerce.Models.Users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // The user who wrote the review

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // The product being reviewed

    @Column(columnDefinition = "TEXT")
    private String reviewText; // The text of the review

    @Column(nullable = false)
    private int rating; // A rating (e.g., 1-5) for the product

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime reviewDate; // The date and time when the review was posted


}
