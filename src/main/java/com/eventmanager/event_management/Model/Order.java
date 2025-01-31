package com.eventmanager.event_management.Model;

import com.eventmanager.event_management.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private int ticketQuantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient
    @Setter
    @Getter
    private String formattedTotalPrice;

    public double getTotalPrice() {
        return ticketQuantity * event.getPrice();
    }

}