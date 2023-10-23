package com.Mirra.eCommerce.Models.datas;

import com.Mirra.eCommerce.Models.DataHistory.ProductStatusHistory;
import com.Mirra.eCommerce.Models.DataHistory.ProductStockHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "description", length = 1000) // Set the maximum length to 1000 characters
    private String description;


    private BigDecimal actualPrice;


    private BigDecimal discountPrice;

    private BigDecimal myPrice;


    private LocalDate expirationDate;


    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive;


    private int stock;   //to see current stock


    private int updateStock;   //if needed to add new stock default set as 0

    @Lob
    @Column(columnDefinition = "LONGBLOB",name ="image_blob",length = Integer.MAX_VALUE)
    private byte[] imageBlob; // Store multiple images as a single BLOB


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "sub_category_id") // This should match the column name in your database table
    private SubCategory subCategory;



    @OneToMany(mappedBy = "product")
    private List<ProductReview> productReviews;



    @Transient // Mark this field as transient to exclude it from database mapping
    private double averageRating;



    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductStatusHistory> stockHistory = new ArrayList<>(); // Initialize the list



    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<ProductStockHistory> currentStock= new ArrayList<>(); // Initialize the list


    public void updateStock(int newUpdateStock) {
        // Create a new stock history entry
        ProductStatusHistory stockHistoryEntry = new ProductStatusHistory();
        stockHistoryEntry.setProducts(this); // Set the current product
        stockHistoryEntry.setStock(newUpdateStock);
        stockHistoryEntry.setChangeTimestamp(LocalDateTime.now()); // Use the current date and time
        // Add the stock history entry to the product's stockHistory list
        this.getStockHistory().add(stockHistoryEntry);
    }



    public void currentStock(int currentStock) {
        // Create a new stock history entry
        ProductStockHistory stockHistoryEntry = new ProductStockHistory();
        stockHistoryEntry.setProducts(this); // Set the current product
        stockHistoryEntry.setCurrentStock(currentStock);
        stockHistoryEntry.setChangeTimestamp(LocalDateTime.now()); // Use the current date and time
        // Add the stock history entry to the product's stockHistory list
        this.getCurrentStock().add(stockHistoryEntry);
    }


    public void checkExpirationDate() {
        LocalDate currentDate = LocalDate.now();
        if (expirationDate != null && currentDate.isAfter(expirationDate)) {
            // If the expiration date is in the past, set discountPrice to 0
            discountPrice = null;
        }
    }


}
