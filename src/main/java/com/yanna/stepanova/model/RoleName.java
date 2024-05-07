package com.yanna.stepanova.model;

public enum RoleName {
    ADMIN("admin"),
    USER("user");
    private final String roleName;

    RoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
