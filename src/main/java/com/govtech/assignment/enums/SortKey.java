package com.govtech.assignment.enums;

public enum SortKey {
    NAME,
    SALARY;

    public static final String ERRORS_INVALID_SORT_KEY = "Invalid Sort Key";

    public static SortKey fromText(String text) {
        if (text == null) return null;
        for (SortKey s : SortKey.values()) {
            if (s.name().equalsIgnoreCase(text)) {
                return s;
            }
        }
        throw new IllegalArgumentException(ERRORS_INVALID_SORT_KEY);
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

