package ru.xenya.market.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;
import ru.xenya.market.ui.utils.converters.CurrencyFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

@Entity
public class PriceItem extends AbstractEntity {

    @NotBlank(message = "{market.name.required}")
    @Size(max = 255)
    @Column(unique = true)
    private String name;

    @NotNull(message = "{market.service.required}")
    private Service service;

    @NotNull(message = "{market.unit.required}")
    private Unit unit;


    //real price * 100 as int to avoid rounding errors
    @Min(value = 0, message = "{market.price.limits}")
    @Max(value = 100000, message = "{market.price.limits}" )
    private Integer price;


    public PriceItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void changeService(Service service) {
        this.service = service;
    }

    public void changeUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return name + "  " + new CurrencyFormatter().encode(price);
    }
}
