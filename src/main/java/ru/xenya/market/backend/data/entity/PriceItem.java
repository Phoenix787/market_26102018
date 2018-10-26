package ru.xenya.market.backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

@Entity
public class PriceItem extends AbstractEntity {

    @NotBlank(message = "{market.name.requered}")
    @Size(max = 255)
    @Column(unique = true)
    private String name;

    //real price * 100 as int to avoid rounding errors
    @Min(value = 0, message = "{market.price.limits}")
    @Max(value = 100000, message = "{market.price.limits}" )
    private Integer price;

    @ManyToOne
    @NotNull
    private Price owner;

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

    public Price getOwner() {
        return owner;
    }

    public void setOwner(Price owner) {
        this.owner = owner;
    }


    @Override
    public String toString() {
        return name + ' ' + price.toString();
    }
}
