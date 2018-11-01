package ru.xenya.market.backend.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PriceItem extends AbstractEntity {

    @NotBlank(message = "{market.name.requered}")
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

//    @ManyToOne
//    @NotNull
//    private Price owner;

//    public PriceItem(Price currentPrice) {
//        setOwner(currentPrice);
//    }

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

//    public Price getOwner() {
//        return owner;
//    }
//
//    public void setOwner(Price owner) {
//        this.owner = owner;
//    }

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
        return name + ' ' + price.toString();
    }
}
