package ru.xenya.market.backend.data.entity;

import ru.xenya.market.backend.data.Discount;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderItem extends AbstractEntity{

   // @NotNull(message = "{market.price.required}")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Price pricePlan;

//    @NotNull
//    private Service service;
//
//    private Unit unit;

    //цена за единицу
   @ManyToOne
//    @JoinColumn
    private PriceItem price;

//    @Min(1d)
    //@NotNull
    private Double quantity = 1d;

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


    public OrderItem() {
    }

    public OrderItem(User createdBy){
        this.dates = new ArrayList<>();
    }

    public Price getPricePlan() {
        return pricePlan;
    }

    public void setPricePlan(Price pricePlan) {
        this.pricePlan = pricePlan;
    }

    public Service getService() {
        return price.getService();
    }
//
//    public void setService(Service service) {
//        this.service = service;
//    }
//
    public Unit getUnit() {
        return price.getUnit();
    }
//
//    public void setUnit(Unit unit) {
//        this.unit = unit;
//    }

    public PriceItem getPrice() {
        return price;
    }

    public void setPrice(PriceItem price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
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
        return quantity == null || price == null || getDates().size() == 0 ? 0 : (int) (quantity * price.getPrice() * getDates().size());
    }
}
