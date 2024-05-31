package com.yanna.stepanova.model;

import java.util.Objects;

public enum Status {
    PENDING("PENDING"),
    DELIVERED("DELIVERED"),
    COMPLETED("COMPLETED");

    private final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public static Status getByType(String type) {
        for (Status item : Status.values()) {
            if (Objects.equals(item.getStatusName(), type)) {
                return item;
            }
        }
        return null;
    }
}
