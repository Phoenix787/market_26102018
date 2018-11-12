package ru.xenya.market.backend.data.entity;

import ru.xenya.market.backend.data.Discount;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class OrderItem extends AbstractEntity{

    @NotNull(message = "{market.price.required}")
    private Price pricePlan;

    @NotNull
    private Service service;

    private Unit unit;

    //цена за единицу
    private Integer price;

    @Min(1)
    @NotNull
    private Integer quantity = 1;

   // private Boolean cash; //наличный = true, безналичный = false;

    //скидка
    private Discount discount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    @OrderColumn
    @JoinColumn
//    @NotEmpty
//    @Valid
    private List<ScheduleDates> dates;

    //сумма
    private Integer totalPrice;

    public Price getPricePlan() {
        return pricePlan;
    }

    public void setPricePlan(Price pricePlan) {
        this.pricePlan = pricePlan;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

//    public Boolean getCash() {
//        return cash;
//    }
//
//    public void setCash(Boolean cash) {
//        this.cash = cash;
//    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public List<ScheduleDates> getDates() {
        return dates;
    }

    public void setDates(List<ScheduleDates> dates) {
        this.dates = dates;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getAllPrice() {
        return quantity == null || price == null || getDates().size() == 0 ? 0 : quantity * price * getDates().size();
    }
}
