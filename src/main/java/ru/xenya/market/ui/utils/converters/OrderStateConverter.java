package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;
import ru.xenya.market.backend.data.OrderState;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;

public class OrderStateConverter implements ModelEncoder<OrderState, String> {

    private Map<String, OrderState> values;


    public OrderStateConverter() {
        values = Arrays.stream(OrderState.values())
                .collect(Collectors.toMap(OrderState::toString, Function.identity()));
    }

    @Override
    public String encode(OrderState modelValue) {
        return convertIfNotNull(modelValue, OrderState::toString);
    }

    @Override
    public OrderState decode(String presentationValue) {
        return convertIfNotNull(presentationValue, values::get);
    }

}
