package ru.xenya.market.backend.data.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity(name = "prices")
@Data
@AllArgsConstructor
public class Price extends AbstractEntity {

    @NotNull(message = "{market.due.dueDate.required}")
    private LocalDate date;

    //множество позиций прайса
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn
    @JoinColumn
    private List<PriceItem> itemsPrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderColumn
    @JoinColumn
    private List<HistoryItem> history;

    private boolean defaultPrice;

    public Price(){

    }
    public Price(User createdBy){
        this.defaultPrice = true;
        this.date = LocalDate.now();
        this.itemsPrice = new ArrayList<>();
    }

    public Price(Price other, User createdBy){
        this.defaultPrice = true;
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

    private void addHistoryItem(User createdBy, String message) {
        HistoryItem item = new HistoryItem(createdBy, message);
        if (history == null) {
            history = new LinkedList<>();
        }
        history.add(item);
    }


}
