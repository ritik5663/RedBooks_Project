package com.redbooks.admin.entity;

public enum SchoolStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    SUSPENDED("Suspended"),
    ACTIVE("Active");

    private final String label;

    SchoolStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
