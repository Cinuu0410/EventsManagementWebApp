package com.eventmanager.event_management.Enum;

public enum EventCategory {
    concert("Koncert"),
    workshops("Warsztaty"),
    conference("Konferencja");

    private final String displayName;

    EventCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
