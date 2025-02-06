package com.eventmanager.event_management.Enum;

import lombok.Getter;

@Getter
public enum OrderStatus {
    Zlozone("Złożone"),
    Zaplacone("Zapłacone"),
    Potwierdzone("Potwierdzone"),
    Wyslane("Wysłane"),
    Dostarczone("Dostarczone"),
    Anulowane("Anulowane");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
}
