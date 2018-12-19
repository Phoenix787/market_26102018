package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.templatemodel.ModelEncoder;

import java.time.LocalTime;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;
import static ru.xenya.market.ui.utils.FormattingUtils.HOUR_FORMATTER;

public class LocalTimeConverter implements ModelEncoder<LocalTime, String>, Converter<String, LocalTime> {

    @Override
    public Result<LocalTime> convertToModel(String value, ValueContext context) {
        return Result.ok(decode(value));
    }

    @Override
    public String convertToPresentation(LocalTime value, ValueContext context) {
        return encode(value);
    }

    @Override
    public String encode(LocalTime modelValue) {
        return convertIfNotNull(modelValue, HOUR_FORMATTER::format);
    }

    @Override
    public LocalTime decode(String presentationValue) {
        return convertIfNotNull(presentationValue, p -> LocalTime.parse(p, HOUR_FORMATTER));
    }
}
