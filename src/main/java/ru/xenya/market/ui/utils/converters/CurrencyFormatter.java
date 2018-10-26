package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;
import ru.xenya.market.ui.utils.FormattingUtils;

public class CurrencyFormatter implements ModelEncoder<Integer, String> {
    @Override
    public String encode(Integer value) {
        return DataProviderUtils.convertIfNotNull(value, FormattingUtils::formatAsCurrency);
    }

    @Override
    public Integer decode(String value) {
        throw new UnsupportedOperationException();
    }
}
