package ru.xenya.market.ui.components.common;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.shared.Registration;
import ru.xenya.market.backend.service.CrudService;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractEditorDialog<T> extends Dialog {
    public enum Operation {
        ADD("Добавление", "добавлен", false), EDIT("Редактирование", "изменён", true);

        private final String nameInTitle;
        private final String nameInText;
        private final boolean deleteEnabled;

        Operation(String nameInTitle, String nameInText, boolean deleteEnabled) {
            this.nameInTitle = nameInTitle;
            this.nameInText = nameInText;
            this.deleteEnabled = deleteEnabled;
        }

        public String getNameInTitle() {
            return nameInTitle;
        }

        public String getNameInText() {
            return nameInText;
        }

        public boolean isDeleteEnabled() {
            return deleteEnabled;
        }
    }

    private final H3 titleField = new H3();
    private final Button saveButton = new Button("Сохранить");
    private final Button cancelButton = new Button("Отменить");
    private final Button deleteButton = new Button("Удалить");
    private Registration registrationForSave;

    private final FormLayout formLayout = new FormLayout();
    private final HorizontalLayout buttonBar = new HorizontalLayout(saveButton, cancelButton, deleteButton);
    private Binder<T> binder = new Binder<>();
    private T currentItem;


    private final ConfirmationDialog<T> confirmDialog = new ConfirmationDialog<>();

    private final String itemType;
    private final BiConsumer<T, Operation> itemSaver;
    private final Consumer<T> itemDeleter;

    public AbstractEditorDialog(String itemType, BiConsumer<T, Operation> itemSaver, Consumer<T> itemDeleter) {
        this.itemType = itemType;
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;

        initTitle();
        initFormLayout();
        initButtonBar();
        setCloseOnEsc(true);
      //  setCloseOnOutsideClick(false);
    }

    protected void initFormLayout() {
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("25em", 2));
        Div div = new Div(formLayout);
        div.addClassName("has-padding");
        add(div);
    }

    private void initButtonBar() {
        saveButton.setAutofocus(true);
        saveButton.getElement().setAttribute("theme", "primary");
        cancelButton.addClickListener(e -> close());
        deleteButton.addClickListener(e -> deleteClicked());
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);
        add(buttonBar);
    }

    private void deleteClicked() {
        if (confirmDialog.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(confirmDialog));
        }
        confirmDelete();
    }

    private void initTitle() {
        add(titleField);
    }

    protected abstract void confirmDelete();

    protected final FormLayout getFormLayout() {  return formLayout; }

    public final Binder<T> getBinder() {  return binder;  }

    public final ConfirmationDialog<T> getConfirmDialog() { return confirmDialog; }

    protected final T getCurrentItem() {  return currentItem;  }


    /**
     *
     * @param item
     * @param operation
     */
    public final void open(T item, Operation operation) {
        currentItem = item;
        titleField.setText(operation.getNameInTitle() + " " + itemType);
        if (registrationForSave != null) {
            registrationForSave.remove();
        }
        registrationForSave = saveButton.addClickListener(e -> saveClicked(operation));
        binder.readBean(currentItem);

        deleteButton.setEnabled(operation.isDeleteEnabled());
        open();
    }

    /**
     *
     * @param operation
     */
    private void saveClicked(Operation operation) {
        boolean isValid = binder.writeBeanIfValid(currentItem);

        if (isValid) {
            itemSaver.accept(currentItem, operation);
            close();
        } else {
            BinderValidationStatus<T> status = binder.validate();
        }
    }

    protected final void openConfirmDialog(String title, String message, String additionalMessage){
       close();
       confirmDialog.open(title, message, additionalMessage, "Удалить", true, getCurrentItem(),
               this::onDelete, this::open);
    }

    private void onDelete(T item) {
        doDelete(item);
    }

    protected void doDelete(T item){
        itemDeleter.accept(item);
        close();
    };
}
