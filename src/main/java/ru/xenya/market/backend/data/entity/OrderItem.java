package ru.xenya.market.backend.data.entity;

import ru.xenya.market.backend.data.Discount;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class OrderItem extends AbstractEntity{

    //цена за единицу
    // @NotNull(message = "{market.price.required}")
   @ManyToOne
//    @JoinColumn
    private PriceItem price;

    @Min(0)
    @NotNull
    private Integer quantity = 100;

    //скидка
    private Discount discount;

    @ManyToMany(/*cascade = CascadeType.MERGE,*/cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
//    @OrderColumn
 //   @JoinColumn
//    @NotEmpty
//    @Valid
    private List<ScheduleDates> dates;

    //сумма
    private Integer totalPrice = 0;


    public OrderItem() {
    }

    public OrderItem(User createdBy){

        this.dates = new ArrayList<>();
    }

    //конструктор копирования
    // конструктор копии
    public OrderItem(OrderItem other) {
        this();
        this.price = other.price;
        //нужно копировать Set дат

    }


    public Service getService() {
        return price.getService();
    }

    public void setService(Service service) { price.setService(service);  }

    public Unit getUnit() {
        return price.getUnit();
    }

    public void setUnit(Unit unit) { price.setUnit(unit);  }

    public PriceItem getPrice() {
        return price;
    }

    public void setPrice(PriceItem price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

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
