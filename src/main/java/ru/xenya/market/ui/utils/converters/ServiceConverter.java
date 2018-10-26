package ru.xenya.market.ui.utils.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;
import ru.xenya.market.backend.data.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.convertIfNotNull;

public class ServiceConverter implements ModelEncoder<Service, String> {
    private Map<String, Service> values;

    public ServiceConverter() {
        values = Arrays.stream(Service.values())
                .collect(Collectors.toMap(Service::toString, Function.identity()));
    }

    @Override
    public String encode(Service modelValue) {
        return convertIfNotNull(modelValue, Service::toString);
    }

    @Override
    public Service decode(String presentationValue) {
        return convertIfNotNull(presentationValue, values::get);
    }
}
