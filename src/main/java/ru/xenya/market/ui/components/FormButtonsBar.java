package ru.xenya.market.ui.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.web.bind.annotation.PutMapping;

@Tag("form-buttons-bar")
@HtmlImport("src/components/form-buttons-bar.html")
public class FormButtonsBar extends PolymerTemplate<TemplateModel> {

    @Id("save")
    private Button save;
    @Id("cancel")
    private Button cancel;
    @Id("delete")
    private Button delete;

    public void setSaveText(String saveText){ save.setText(saveText == null ? "" : saveText);}

    public void setCancelText(String cancelText) {cancel.setText(cancelText== null ? "" : cancelText);}

    public void setDeleteText(String deleteText) { delete.setText(deleteText == null ? "" : deleteText);}

    public void setSaveDisabled(boolean saveDisabled) { save.setEnabled(!saveDisabled);}

    public void setCancelDisabled(boolean cancelDisabled) { cancel.setEnabled(!cancelDisabled); }

    public void setDeleteDisabled(boolean deleteDisabled) { delete.setEnabled(!deleteDisabled);}

    public static class SaveEvent extends ComponentEvent<FormButtonsBar> {
        public SaveEvent(FormButtonsBar source, boolean fromClient){
            super(source, fromClient);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener){
        return save.addClickListener(e -> listener.onComponentEvent(new SaveEvent(this, true)));
    }

    public static class CancelEvent extends ComponentEvent<FormButtonsBar>{

        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         */
        public CancelEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return cancel.addClickListener(e -> listener.onComponentEvent(new CancelEvent(this, true)));
    }

    public static class DeleteEvent extends ComponentEvent<FormButtonsBar> {

        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         */
        public DeleteEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener){
        return delete.addClickListener(e -> listener.onComponentEvent(new DeleteEvent(this, true)));
    }

}
