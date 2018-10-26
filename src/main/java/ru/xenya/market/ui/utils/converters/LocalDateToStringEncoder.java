package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;
import ru.xenya.market.ui.utils.FormattingUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;

public class LocalDateToStringEncoder implements ModelEncoder<LocalDate, String> {

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String encode(LocalDate value) {
        return convertIfNotNull(value, FormattingUtils.FULL_DAY_FORMATTER::format);
    }

    @Override
    public LocalDate decode(String value) {
        return LocalDate.parse(value, DATE_FORMAT);
       // throw new UnsupportedOperationException();
    }
}
