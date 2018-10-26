package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;
import ru.xenya.market.backend.data.Unit;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;

public class UnitConverter implements ModelEncoder<Unit, String> {

    private Map<String, Unit> values;

    public UnitConverter() {
        values = Arrays.stream(Unit.values())
                .collect(Collectors.toMap(Unit::toString, Function.identity()));
    }

    @Override
    public String encode(Unit modelValue) {
        return convertIfNotNull(modelValue, Unit::toString);
    }

    @Override
    public Unit decode(String presentationValue) {
        return convertIfNotNull(presentationValue, values::get);
    }
}
