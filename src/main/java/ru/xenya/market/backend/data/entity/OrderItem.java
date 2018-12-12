package ru.xenya.market.backend.data.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;
import ru.xenya.market.backend.data.Discount;
import ru.xenya.market.backend.data.Service;
import ru.xenya.market.backend.data.Unit;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class OrderItem extends AbstractEntity {

    //цена за единицу   {market.price.required}
    @NotNull(message = "выберите цену за единицу")
    @ManyToOne(fetch = FetchType.LAZY)
    @Valid
//    @JoinColumn
    private PriceItem price;

    @Min(value = 100, message = "{market.quantity.required}" )
    @NotNull(message = "{market.quantity.required}")
    @Valid
    private Integer quantity = 100;

    private Integer width = 0;
    private Integer height = 0;


    //скидка
    private Discount discount;

    @ManyToMany/*(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE*//*CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE*//*},
            fetch = FetchType.LAZY)*/
    @JoinTable(name = "ORDER_ITEM_DATES",
            joinColumns = {@JoinColumn(name = "order_item_id")},
            inverseJoinColumns =  @JoinColumn(name = "dates_id"))
    @NotEmpty
    @Valid
//    private List<ScheduleDates> dates;
    private Set<ScheduleDates> dates;

    //сумма
    @Min(1)
    @NotNull(message = "{market.price.limits}")
    @Valid
    private Integer totalPrice = 0;


    public OrderItem() {
    }

    public OrderItem(User createdBy) {
        this.discount = Discount.none;
//        this.dates = new ArrayList<>();
        this.dates = new HashSet<>();
    }

    //конструктор копирования
    public OrderItem(OrderItem other) {
        this();
        this.price = other.price;
        Set<ScheduleDates> items = new HashSet<>();
        Iterator<ScheduleDates> iterator = other.dates.iterator();
        ScheduleDates item;
        while (iterator.hasNext()){
            item = iterator.next();
            items.add(item);
        }
        this.setId(other.getId());
        this.dates = items;
        this.discount = other.discount;
        this.totalPrice = other.totalPrice;
        this.quantity = other.quantity;
        this.width = other.width;
        this.height= other.height;
        //нужно копировать Set дат

    }


    public Service getService() {
        return price.getService();
    }

    public void setService(Service service) {
        price.setService(service);
    }

    public Unit getUnit() {
        return price.getUnit();
    }

    public void setUnit(Unit unit) {
        price.setUnit(unit);
    }

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

    public Set<ScheduleDates> getDates() {
        return dates;
    }

    public void setDates(Set<ScheduleDates> dates) {
        this.dates = dates;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public int getAllPrice() {
        return quantity == null || price == null || getDates().size() == 0 ? 0 : (int) (quantity * price.getPrice() * getDates().size());
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "price=" + price +
                ", quantity=" + quantity +
                ", width=" + width +
                ", height=" + height +
                ", discount=" + discount +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
