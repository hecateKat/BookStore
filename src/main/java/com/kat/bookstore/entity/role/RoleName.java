package com.kat.bookstore.entity.role;

public enum RoleName {
    ROLE_USER("ROLE_USER"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String roleName;

    RoleName(String roleName) {
        this.roleName = roleName;
    }
}
