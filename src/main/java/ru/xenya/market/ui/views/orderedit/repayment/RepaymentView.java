package ru.xenya.market.ui.views.orderedit.repayment;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.Repayment;
import ru.xenya.market.ui.utils.FormattingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Designer generated component for the repayment-view.html template.
 * <p>
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("repayment-view")
@HtmlImport("src/views/orderedit/repayment/repayment-view.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RepaymentView extends PolymerTemplate<RepaymentView.RepaymentViewModel>
        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<RepaymentView, List<Repayment>>, List<Repayment>> {

    //  private Dialog dialog;

    private final AbstractFieldSupport<RepaymentView, List<Repayment>> fieldSupport;
    private boolean hasChanges = false;
    private Integer totalSum = 0;
    private Order currentOrder;

    private RepaymentEditor editor;

    @Id("grid")
    private Grid<Repayment> grid;
    @Id("add")
    private Button add;
    @Id("deleteBtn")
    private Button deleteBtn;
    @Id("totalSumSpan")
    private Span totalSumSpan;
    @Id("dialog")
    private Dialog dialog;


    /**
     * Creates a new RepaymentView.
     */
    public RepaymentView() {

        this.editor = new RepaymentEditor();
        fieldSupport = new AbstractFieldSupport<>(this, new ArrayList<>(),
                Objects::equals, c -> {
        });
//        dialog.getElement().addAttachListener(event -> UI.getCurrent().getPage().executeJavaScript(
//                "$0.$.overlay.setAttribute('theme', 'center');", dialog.getElement()
//        ));

        setupGrid();
        setupListeners();

        deleteBtn.setEnabled(false);
    }

    private void setupListeners() {
        deleteBtn.addClickListener(e -> {
            grid.getSelectionModel().getFirstSelectedItem().ifPresent(item -> {
                setValue(getValue().stream().filter(element -> element != item).collect(Collectors.toList()));
                updateTotalPrice(getValue());
                //пускаем событие, чтобы пересчитать totalSum платежей
                //fireEvent(new DeleteRepaymentEvent(this, item));
            });
        });
//
        add.addClickListener(e -> createNew());
        editor.addCancelListener(e -> dialog.setOpened(false));
        editor.addSaveListener(e -> save(editor.getValue()));
    }

    private void createNew() {
        editor.read(new Repayment(), true);
        dialog.add(editor);
        dialog.open();
    }

    //
    private void save(Repayment entity) {
        List<Repayment> items = Stream.concat(getValue().stream(), Stream.of(entity)).collect(Collectors.toList());
        setValue(items);
        updateTotalPrice(items);
        dialog.setOpened(false);
    }

    //
    private void updateTotalPrice(List<Repayment> items) {
        totalSum = 0;
        totalSum = items.stream().map(Repayment::getSum).reduce((x, y) -> x + y).orElse(0);
        setTotalSum(totalSum);
     //   totalSumSpan.setText(FormattingUtils.formatAsCurrency(totalSum));
        setHasChanges(true);
        // fireEvent(new TotalSumEvent(this, totalSum));
    }

    //
    private void setupGrid() {
        grid.setHeightByRows(true);
        grid.addColumn(Repayment::getDate).setWidth("70px").setHeader("Дата").setResizable(true);
        grid.addColumn(item -> FormattingUtils.formatAsCurrency(item.getSum())).setWidth("70px").setHeader("Сумма").setResizable(true);
        grid.addColumn(Repayment::getPayment).setWidth("70px").setHeader("Оплата").setResizable(true);
        grid.addSelectionListener(e -> {
            deleteBtn.setEnabled(true);
        });

    }

    @Override
    public List<Repayment> getValue() {
        return fieldSupport.getValue();
    }

    @Override
    public void setValue(List<Repayment> repayments) {
        fieldSupport.setValue(repayments);
        hasChanges = false;
        if (repayments != null) {
            grid.setItems(repayments);
            setTotalSum(repayments.stream().map(Repayment::getSum).reduce(0, Integer::sum));
        } else{
            setTotalSum(0);
        }

    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<RepaymentView, List<Repayment>>> valueChangeListener) {
        return fieldSupport.addValueChangeListener(valueChangeListener);
    }

    private void setHasChanges(boolean hasChanges) {
        this.hasChanges = hasChanges;
        if (hasChanges) {
            fireEvent(new ru.xenya.market.ui.views.orderedit.repayment.ValueChangeEvent(this));
        }
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public void clear() {
        deleteBtn.setEnabled(false);
        setTotalSum(0);
        setValue(null);
        totalSum = 0;
    }

    private void setTotalSum(int totalSum) {
        getModel().setTotalSum(FormattingUtils.formatAsCurrency(totalSum));
    }

    /**
     * This model binds properties between RepaymentView and repayment-view.html
     */
    public interface RepaymentViewModel extends TemplateModel {
        // Add setters and getters for template properties here.
        void setTotalSum(String totalSum);
    }
}

//public class RepaymentView extends VerticalLayout
//        implements HasValueAndElement<AbstractField.ComponentValueChangeEvent<RepaymentView, List<Repayment>>, List<Repayment>> {
//
////    private Grid<Repayment> grid;
////    private Button addRepayment;
////    private Button deleteRepayment;
////    private Span totalSumSpan;
//    private Dialog dialog;
//
//    private boolean hasChanges = false;
//    private final AbstractFieldSupport<RepaymentView, List<Repayment>> fieldSupport;
//
//    private Integer totalSum = 0;
//    private Order currentOrder;
//
//    private RepaymentEditor editor;
//    @Id("grid")
//    private Grid<Repayment> grid;
//    @Id("addPay")
//    private Button addPay;
//    @Id("add")
//    private Button add;
//    @Id("deleteBtn")
//    private Button deleteBtn;
//    @Id("totalSumSpan")
//    private Span totalSumSpan;
//    @Id("add")
//    private Button vaadinButton;
//
//    public RepaymentView() {
//        fieldSupport = new AbstractFieldSupport<>(this, new ArrayList<>(),
//                Objects::equals, c -> {
//        });
//
//        totalSumSpan = new Span();
//        grid = new Grid<>();
////        addRepayment = new Button(VaadinIcon.PLUS.create());
////        deleteRepayment = new Button(VaadinIcon.MINUS.create());
////        delete.setEnabled(false);
//        dialog = new Dialog();
//
//        this.editor = new RepaymentEditor();
//        dialog.add(editor);
//
//        setupGrid();
//        setupListeners();
//
////        expand(grid);
////        add(grid, new HorizontalLayout(addRepayment, deleteRepayment, totalSumSpan));
//    }
//

//}
