package com.redbooks.admin.entity;

public enum PublisherStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    ACTIVE("Active");

    private final String label;

    PublisherStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
