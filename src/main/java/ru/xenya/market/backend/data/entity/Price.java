package ru.xenya.market.backend.data.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.xenya.market.ui.utils.converters.LocalDateToStringEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


@Entity(name = "prices")
public class Price extends AbstractEntity implements PriceSummary {

    @NotNull(message = "{market.due.dueDate.required}")
    private LocalDate date;

    //множество позиций прайса
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn
    @JoinColumn(name = "item_id")
    private List<PriceItem> itemsPrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /*todo разобраться с lazy failed to lazily initialize a collection of role:
    ru.xenya.market.backend.data.entity.Price.history, could not initialize proxy -
     no Session
    */
    @OrderColumn
    @JoinColumn
    private List<HistoryItem> history;

    private boolean defaultPrice = false;

    public Price(){
        //Empty constructor is needed by Spring Data / JPA
    }
    public Price(User createdBy){
        this.defaultPrice = true;
        this.date = LocalDate.now();
        addHistoryItem(createdBy, "Прайс создан");
        this.itemsPrice = new ArrayList<>();
    }
    //копирующий конструктор
    public Price(Price other, User createdBy){
        this.date = LocalDate.now();
        ArrayList<PriceItem> itemsPrice = new ArrayList<>();
        Iterator<PriceItem> iterator = other.itemsPrice.iterator();
        PriceItem itemPrice;
        while (iterator.hasNext()){
            itemPrice = iterator.next();
            itemsPrice.add(itemPrice);
        }
        this.itemsPrice = itemsPrice;
    }


    public void changeDefault(User user, boolean isDefault){
        boolean createHistory = this.defaultPrice != isDefault;
        this.defaultPrice = isDefault;
        if (createHistory) {
            String message;

            if (isDefault){
                message = "Прайс установлен по умолчанию";
            } else {
                message = "Статус прайса - не по умолчанию";
            }
            addHistoryItem(user, message);
        }
    }

    public void addHistoryItem(User createdBy, String message) {
        HistoryItem item = new HistoryItem(createdBy, message);
        if (history == null) {
            history = new LinkedList<>();
        }
        history.add(item);
    }

    @Override
    public Boolean isDefault() {
        return defaultPrice;
    }

    @Override
    public List<PriceItem> getItems() {
        return itemsPrice;
    }

    @Override
    public String toString() {
        return new LocalDateToStringEncoder().encode(date);
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<PriceItem> getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(List<PriceItem> itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryItem> history) {
        this.history = history;
    }

    public boolean isDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(boolean defaultPrice) {
        this.defaultPrice = defaultPrice;
    }
}
