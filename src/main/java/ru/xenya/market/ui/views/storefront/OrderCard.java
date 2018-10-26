package ru.xenya.market.ui.views.storefront;

import com.vaadin.flow.data.renderer.TemplateRenderer;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.OrderSummary;
import ru.xenya.market.ui.utils.converters.OrderStateConverter;

import java.time.LocalDate;
import java.util.List;

/**
 * Help class to get ready to use TemplateRenderer for displaying order card list on the Storefront and Dashboard grids.
 * Using TemplateRenderer instead of ComponentRenderer optimizes the CPU and memory consumption.
 * <p>
 * In addition, component includes an optional header above the order card. It is used
 * to visually separate orders into groups. Technically all order cards are
 * equivalent, but those that do have the header visible create a visual group
 * separation.
 * Класс-помощник, чтобы получить готовый к использованию TemplateRenderer
 * для показа списка карточек заказа на витрине и Dashboard.
 * Использование Templaterendererer вместо ComponentRenderer оптимизирует потребление процессора и памяти.
 * Кроме того, компонент включает дополнительный заголовок над карточкой заказа.
 * Используется для визуального разделения заказов на группы. Технически все карты заказов эквивалентны,
 * но те, которые имеют видимый заголовок, создают визуальное разделение групп.
 */
public class OrderCard {

    public static TemplateRenderer<Order> getTemplate() {
        return TemplateRenderer.of(
                "<order-card"
                + " header='[[item.header]]'"
                + " order-card='[[item.orderCard]]'"
                + "on-card-click='cardClick'>"
                + "</order-card>");
    }

    public static OrderCard create(OrderSummary order) {
        return new OrderCard(order);
    }

    public static OrderStateConverter stateConverter = new OrderStateConverter();

    private boolean recent, inWeek;

    private final OrderSummary order;

    public OrderCard(OrderSummary order) {
        this.order = order;
        LocalDate now = LocalDate.now();
        LocalDate date = order.getDueDate();
        recent = date.equals(now) || date.equals(now.minusDays(1));
       // inWeek = !recent && now.getYear() == date.getYear() && now.get(WEEK_IN_YEAR_FIELD) == date.get(WEEK_IN_YEAR_FIELD);


    }

    public String getState(){
        return stateConverter.encode(order.getState());
    }

    public String getFullName() {
        return order.getCustomer().getFullName();
    }

//    public List<OrderItem> getItems(){
//        return order.getItems();
//    }


}
