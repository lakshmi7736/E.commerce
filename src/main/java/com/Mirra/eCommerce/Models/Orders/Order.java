package com.Mirra.eCommerce.Models.Orders;

import com.Mirra.eCommerce.Models.Users.Address;
import com.Mirra.eCommerce.Models.Users.Payment;
import com.Mirra.eCommerce.Models.Users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;


    //    private LocalDateTime orderDate;
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderStatusHistory> statusHistory;



    private BigDecimal purchaseTotal;


    private BigDecimal granTotal;

    private BigDecimal redeemedAmount;

    private boolean redeemedFromWallet;


    @Enumerated(EnumType.STRING) // Use EnumType.STRING to store enum values as strings
    private Payment method;


    @Enumerated(EnumType.STRING) // Use EnumType.STRING to store enum values as strings
    private OrderStatus status;


    private String paymentId;

    public void updateOrderStatus(OrderStatus newStatus) {
        // Create a new OrderStatusHistory entry
        OrderStatusHistory statusHistoryEntry = new OrderStatusHistory();
        statusHistoryEntry.setOrder(this);
        statusHistoryEntry.setNewStatus(newStatus);
        statusHistoryEntry.setChangeTimestamp(LocalDateTime.now());

        // Update the current status of the order
        this.setStatus(newStatus);

        // Add the history entry to the list
        statusHistory.add(statusHistoryEntry);
    }

}
