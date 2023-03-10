package com.toy.overall_practice.domain.role;

public enum RoleType {
    MEMBER("MEMBER");

    private final String role;

    public String getRole() {
        return role;
    }

    RoleType(String role) {
        this.role = role;
    }
}
