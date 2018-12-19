package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;

import java.time.LocalDateTime;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;
import static ru.xenya.market.ui.utils.FormattingUtils.FULL_DATE_FORMATTER;

public class LocalDateTimeConverter implements ModelEncoder<LocalDateTime, String> {

    private static final LocalTimeConverter TIME_FORMATTER = new LocalTimeConverter();
    @Override
    public String encode(LocalDateTime modelValue) {
        return convertIfNotNull(modelValue,
                v -> FULL_DATE_FORMATTER.format(v) + " " + TIME_FORMATTER.encode(v.toLocalTime()));
    }

    @Override
    public LocalDateTime decode(String presentationValue) {
        throw new UnsupportedOperationException();
    }
}
