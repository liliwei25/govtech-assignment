package com.govtech.assignment.enums;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class SortKeyTests {
    @Test
    void testFromText() {
        assertThat(SortKey.fromText("name")).isEqualTo(SortKey.NAME);
        assertThat(SortKey.fromText("NAME")).isEqualTo(SortKey.NAME);
    }

    @Test
    public void testToString() {
        SortKey sortKey = SortKey.NAME;
        assertThat(sortKey.toString()).isEqualTo("name");
    }
}
