package com.govtech.assignment.utilities;

import com.govtech.assignment.enums.SortKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
class SortKeyConverter<T> implements Converter<String, SortKey> {
    @Override
    public SortKey convert(String value) {
        return SortKey.fromText(value);
    }
}
