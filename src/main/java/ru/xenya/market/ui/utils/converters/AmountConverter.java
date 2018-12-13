package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import ru.xenya.market.ui.utils.FormattingUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;

public class AmountConverter implements Converter<String, Integer> {

    private final DecimalFormat df = FormattingUtils.getUiAmountFormatter();


    @Override
    public com.vaadin.flow.data.binder.Result<Integer> convertToModel(String presentationValue, ValueContext context) {
        try {
            return Result.ok((int) Math.round((df.parse(presentationValue).doubleValue())*100));
        } catch (ParseException e) {
            return Result.error("Неправильное число");
        }
    }

    @Override
    public String convertToPresentation(Integer value, com.vaadin.flow.data.binder.ValueContext context) {
        return convertIfNotNull(value, i->df.format(BigDecimal.valueOf(i, 2)), ()->"");
    }
}
