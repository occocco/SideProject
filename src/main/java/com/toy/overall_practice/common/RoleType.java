package com.toy.overall_practice.common;

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
