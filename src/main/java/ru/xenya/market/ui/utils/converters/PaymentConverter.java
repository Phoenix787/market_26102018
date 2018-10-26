package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;

public class PaymentConverter implements ModelEncoder<Payment, String> {

    private Map<String, Payment> values;


    public PaymentConverter() {
        values = Arrays.stream(Payment.values())
                .collect(Collectors.toMap(Payment::toString, Function.identity()));
    }

    @Override
    public String encode(Payment modelValue) {
        return convertIfNotNull(modelValue, Payment::toString);
    }

    @Override
    public Payment decode(String presentationValue) {
        return convertIfNotNull(presentationValue, values::get);
    }

}
