package com.govtech.assignment.enums;

public enum SortKey {
    NAME,
    SALARY;

    public static SortKey fromText(String text) {
        if (text == null) return null;
        for (SortKey s : SortKey.values()) {
            if (s.name().equalsIgnoreCase(text)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid Sort Key");
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

