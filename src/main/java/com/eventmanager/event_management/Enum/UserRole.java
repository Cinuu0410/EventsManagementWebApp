package com.eventmanager.event_management.Enum;

public enum UserRole {
    ADMINISTRATOR("Administrator"),
    UZYTKOWNIK("Uzytkownik"),
    MODERATOR("Moderator");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
