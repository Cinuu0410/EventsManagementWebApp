package com.eventmanager.event_management.Enum;

public enum EventCategory {
    CONCERT("Koncert"),
    WORKSHOP("Warsztaty"),
    CONFERENCE("Konferencja");

    private final String displayName;

    EventCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
