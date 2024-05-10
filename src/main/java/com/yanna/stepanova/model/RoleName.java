package com.yanna.stepanova.model;

import java.util.Objects;

public enum RoleName {
    ROLE_USER("ROLE_USER"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String roleName;

    RoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static RoleName getByType(String type) {
        for (RoleName item : RoleName.values()) {
            if (Objects.equals(item.getRoleName(), type)) {
                return item;
            }
        }
        return null;
    }
}
