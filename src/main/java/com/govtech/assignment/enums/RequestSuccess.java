package com.govtech.assignment.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestSuccess {
    FAILED(0),
    SUCCESS(1);

    private final Integer value;

    RequestSuccess(Integer value) {
        this.value = value;
    }

    @JsonValue
    public Integer toValue() {
        return this.value;
    }
}
