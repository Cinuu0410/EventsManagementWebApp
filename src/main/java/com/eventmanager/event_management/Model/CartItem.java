package com.eventmanager.event_management.Model;

import lombok.Data;

@Data
public class CartItem {
    private Long eventId;
    private String eventName;
    private int quantity;
    private double price;

    public double getTotalPrice() {
        return quantity * price;
    }
}
