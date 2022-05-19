package com.govtech.assignment.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SortKeyTests {
    @Test
    void testFromText() {
        assertThat(SortKey.fromText("name")).isEqualTo(SortKey.NAME);
        assertThat(SortKey.fromText("NAME")).isEqualTo(SortKey.NAME);
    }

    @Test()
    void testFromTextThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> SortKey.fromText("test"));
    }

    @Test
    void testToString() {
        SortKey sortKey = SortKey.NAME;
        assertThat(sortKey.toString()).isEqualTo("name");
    }
}
