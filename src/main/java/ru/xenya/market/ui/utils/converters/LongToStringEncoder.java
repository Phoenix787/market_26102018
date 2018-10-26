package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;

public class LongToStringEncoder implements ModelEncoder<Long, String> {
    @Override
    public String encode(Long value) {
        return value.toString();
    }

    @Override
    public Long decode(String value) {
        return Long.parseLong(value);
    }
}
