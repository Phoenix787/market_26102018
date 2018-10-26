package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.ui.utils.converters.*;
import ru.xenya.market.ui.events.CancelEvent;
import ru.xenya.market.ui.events.EditEvent;
import ru.xenya.market.ui.events.SaveEvent;
import ru.xenya.market.ui.views.storefront.events.CommentEvent;

@Tag("order-details")
public class OrderDetails extends PolymerTemplate<OrderDetails.Model> {
    private Order order;

    private Button back;
    private Button cancel;
    private Button save;
    private Button edit;
    private Element history;
    private Element comment;
    private Button sendComment;
    private TextField commentField;

    private boolean isDirty;

    public OrderDetails() {
        sendComment.addClickListener(e->{
            String message = commentField.getValue();
            message = message == null ? "" : message.trim();
            if (!message.isEmpty()){
                commentField.clear();
                fireEvent(new CommentEvent(this, order.getId(), message));
            }
        });
        save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
        cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        edit.addClickListener(e -> fireEvent(new EditEvent(this)));
    }

    public void display(Order order, boolean review) {
        getModel().setReview(review);
        this.order = order;
        getModel().setItem(order);
        if (!review){
            commentField.clear();
        }
        this.isDirty = review;
    }

    public boolean isDirty(){
        return isDirty;
    }

    public void setDirty(boolean isDirty){
        this.isDirty = isDirty;
    }

    public interface Model extends TemplateModel{
        @Include({"id", "dueDate.date", "orderState", "payment", "customer.fullName", "customer.phoneNumbers",
        "items.product.name", "items.quantity", "items.product.price", "history.message", "history.createdBy.firstName",
        "history.timestamp", "totalPrice"})
        @Encode(value = LongToStringEncoder.class, path = "id")
        @Encode(value = LocalDateToStringEncoder.class, path = "dueDate")
        @Encode(value = OrderStateConverter.class, path = "state")
        @Encode(value = PaymentConverter.class, path = "payment")
        @Encode(value = CurrencyFormatter.class, path = "items.product.price")
        @Encode(value = CurrencyFormatter.class, path = "totalPrice")
        void setItem(Order order);

        void setReview(boolean review);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addEditListener(ComponentEventListener<EditEvent> listener) {
        return addListener(EditEvent.class, listener);
    }

    public Registration addBackListener(ComponentEventListener<ClickEvent<Button>> listener) {
        return back.addClickListener(listener);
    }

    public Registration addCommentListener(ComponentEventListener<CommentEvent> listener){
        return addListener(CommentEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }
}
