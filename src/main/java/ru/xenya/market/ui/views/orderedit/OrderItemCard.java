package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.data.renderer.TemplateRenderer;
import ru.xenya.market.backend.data.entity.OrderItem;
import ru.xenya.market.backend.data.entity.OrderSummary;
import ru.xenya.market.backend.data.entity.ScheduleDates;
import ru.xenya.market.ui.utils.FormattingUtils;

import java.util.List;

public class OrderItemCard {

    public static TemplateRenderer<OrderItem> getTemplate(){
        return TemplateRenderer.of(
                "<order-item-card"
                + "  header='[[item.header]]'"
                + "  order-item-card='[[item.orderCard]]'"
                + "  on-card-click='cardClick'>" +
                        "</order-item-card>"
        );
    }

    public static OrderItemCard create(OrderItem item){ return new OrderItemCard(item);}

    private final OrderItem item;

    public OrderItemCard(OrderItem item) {
        this.item = item;
    }

//    public List<ScheduleDates> getDates(){
//        return item.getDates();
//    }

    public String getService(){
        return item.getService().toString();
    }

//    public String getCountDates(){
//        return FormattingUtils.formatListSize(getDates());
//    }

    public String getTotalPrice(){
        return FormattingUtils.formatAsCurrency(item.getTotalPrice());
    }

    public String getMeasure(){
        return item.getQuantity() + " " + item.getUnit().toString();
    }
}
