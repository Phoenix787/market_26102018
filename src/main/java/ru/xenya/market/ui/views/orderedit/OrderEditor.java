package ru.xenya.market.ui.views.orderedit;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import org.vaadin.reports.PrintPreviewReport;
import ru.xenya.market.MarketApplication;
import ru.xenya.market.backend.data.OrderState;
import ru.xenya.market.backend.data.Payment;
import ru.xenya.market.backend.data.entity.*;
import ru.xenya.market.backend.data.entity.util.OrderItemSummary;
import ru.xenya.market.ui.components.FormButtonsBar;
import ru.xenya.market.ui.crud.CrudView.CrudForm;
import ru.xenya.market.ui.dataproviders.DataProviderUtils;
import ru.xenya.market.ui.dataproviders.PriceDataProvider;
import ru.xenya.market.ui.events.*;
import ru.xenya.market.ui.utils.FormattingUtils;
import ru.xenya.market.ui.utils.MarketConst;
import ru.xenya.market.ui.utils.TemplateUtils;
import ru.xenya.market.ui.utils.converters.*;
import ru.xenya.market.ui.views.SpecReports;
import ru.xenya.market.ui.views.orderedit.invoice.InvoiceEditor;
import ru.xenya.market.ui.views.orderedit.orderitem.OrderItemsView;
import ru.xenya.market.ui.views.orderedit.orderitem.ValueChangeEvent;
import ru.xenya.market.ui.views.orderedit.repayment.RepaymentView;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static ru.xenya.market.ui.dataproviders.DataProviderUtils.createItemLabelGenerator;

@Tag("order-editor")
@HtmlImport("src/views/orderedit/order-editor.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderEditor extends PolymerTemplate<OrderEditor.Model>
        implements CrudForm<Order> /*AbstractEditorDialog<Order>*/ {


    @Id("title")
    private H2 title;
    @Id("metaContainer")
    private Div metaContainer;
    @Id("orderNumber")
    private Span orderNumber;
    @Id("status")
    private ComboBox<OrderState> status;
    @Id("dueDate")
    private DatePicker dueDate;
    @Id("payment")
    private ComboBox<Payment> payment;
    @Id("cancel")
    private Button cancel;
    @Id("save")
    private Button save;
    @Id("delete")
    private Button delete;
    @Id("itemsContainer")
    private Div itemsContainer;
    @Id("needInvoice")
    private Checkbox needInvoice;
    @Id("invoiceContainer")
    private Div invoiceContainer;
    @Id("pricePlan")
    private ComboBox<Price> pricePlan;
    @Id("history")
    private FormItem history;
    @Id("customer")
    private Span customer;
    @Id("paysContainer")
    private Div paysContainer;

//    @Id("specBtn")
//    private Button specBtn;

    @Id("divSpec")
    private Div divSpec;

//    @Id("dialog")
//    private Dialog dialog;


    private InvoiceEditor invoiceEditor;
    private OrderItemsView orderItemsView;
    private RepaymentView repaymentView;

    private User currentUser;
    private Order currentOrder;
    private Customer currentCustomer;
    private Price defaultPrice;
    private BeanValidationBinder<Order> binder = new BeanValidationBinder<>(Order.class);
    private LocalDateToStringEncoder localDateToStringEncoder = new LocalDateToStringEncoder();

    private JasperPrint jasperPrint;
    private SpecReports specReports;


    @Autowired
    public OrderEditor(OrderItemsView orderItemsView,
                       PriceDataProvider priceDataProvider,
                       RepaymentView repaymentView) {
        this.orderItemsView = orderItemsView;
        this.repaymentView = repaymentView;
        paysContainer.add(repaymentView);
        defaultPrice = priceDataProvider.getDefaultPrice();
        orderItemsView.setDefaultPrice(defaultPrice);
        itemsContainer.add(orderItemsView);


        invoiceEditor = new InvoiceEditor();

        cancel.addClickListener(e -> fireEvent(new CancelEvent(this, false)));
        save.setEnabled(false);
        save.addClickListener(e -> fireEvent(new SaveEvent(this, false)));
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, false)));


        status.setItemLabelGenerator(createItemLabelGenerator(OrderState::toString));
        status.setDataProvider(DataProvider.ofItems(OrderState.values()));
        status.addValueChangeListener(
                e -> getModel().setStatus(DataProviderUtils.convertIfNotNull(e.getValue(), OrderState::toString)));

        binder.forField(status)
                .withValidator(new BeanValidator(Order.class, "orderState"))
                .bind(Order::getOrderState, (o, s) -> {
                    o.changeState(currentUser, s);
                });

        dueDate.setI18n(TemplateUtils.setupDatePicker());
        dueDate.setLocale(Locale.UK);
        dueDate.setRequired(true);
        binder.bind(dueDate, "dueDate");

        payment.setItemLabelGenerator(createItemLabelGenerator(Payment::toString));
        payment.setDataProvider(DataProvider.ofItems(Payment.values()));
        binder.bind(payment, "payment");
        payment.setRequired(true);

        needInvoice.addValueChangeListener(e -> addInvoice(e.getValue()));
        binder.bind(invoiceEditor, "invoice");

      //  binder.bind(customerName, "customer.fullName");

        if (currentOrder != null) {
          //  customerName.setValue(binder.getBean().getCustomer().getFullName());

            if (currentOrder.getInvoice() != null) {
                invoiceEditor.setCurrentInvoice(currentOrder.getInvoice());
            }
        }

        ComponentUtil.addListener(invoiceEditor, ru.xenya.market.ui.views.orderedit.invoice.ValueChangeEvent.class, e -> save.setEnabled(true));
        ComponentUtil.addListener(orderItemsView, ValueChangeEvent.class, e -> save.setEnabled(true));
        ComponentUtil.addListener(repaymentView, ValueChangeEvent.class, e -> save.setEnabled(true));

        orderItemsView.addPriceChangeListener(e -> setTotalPrice(e.getTotalPrice()));

        binder.bind(orderItemsView, "items");
        binder.bind(repaymentView, "repayments");
        binder.addValueChangeListener(e -> {
            if (e.getOldValue() != null) {
                save.setEnabled(true);
            }
        });


        pricePlan.addValueChangeListener(e -> {
            Price price = e.getValue();
            if (price != null && currentOrder != null) {
                if (currentOrder.getItems().size() == 0) {
                    setDefaultPrice(price);
                    getModel().setPricePlan(DataProviderUtils.convertIfNotNull(price, Price::toString));
                } else {
                    pricePlan.setValue(e.getOldValue());
                }
            }
        });

        pricePlan.setDataProvider(priceDataProvider);
      //  pricePlan.setItemLabelGenerator(Price::toString);

        binder.bind(pricePlan, "pricePlan");

//        dialog.setHeight("100vh");
//        dialog.setWidth("50vw");
//        //divSpec.add(dialog);
//        specBtn.addClickListener(e->{
//            //specReports.setCurrentOrder(currentOrder);
//            SpecReports spr = new SpecReports(currentOrder);
//            //specReports.open();
//            dialog.removeAll();
//            dialog.add(spr);
//            dialog.setOpened(true);
//            // specBtn.getUI().ifPresent(ui -> ui.navigate("report"));
//        });

        StreamResource streamResource = new StreamResource("report.pdf", ()->export(currentOrder));
        Anchor anchor = new Anchor(streamResource, "");
        anchor.setTarget("_blank");
        anchor.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
        divSpec.add(anchor);
 }


    private void addInvoice(Boolean value) {
        invoiceEditor.setInvoiceEnabled(value);
        //   invoiceEditor.setHasChanges(true);
    }

    public void close() {
        setTotalPrice(0);
        currentOrder = null;
        invoiceEditor.clear();
        save.setEnabled(false);
        repaymentView.clear();
        getModel().setItem(null);
    }


    public void write(Order order) throws ValidationException {
        binder.writeBean(order);
    }

    public void read(Order order, boolean isNew) {
//        if (order.getRepayments() == null) {
//            order.setRepayments(new ArrayList<>());
//        }
        if (isNew) {
            order.setCustomer(currentCustomer);
            order.setPricePlan(defaultPrice);
            pricePlan.setReadOnly(false);
        } else {
            orderItemsView.setDefaultPrice(order.getPricePlan());
            repaymentView.setCurrentOrder(order);

            if (order.getItems().size() != 0) {
                pricePlan.setReadOnly(true);
            }
            getModel().setItem(order);
            setTotalPrice(order.getTotalPrice());
        }

        if (order.getInvoice() != null) {
            needInvoice.setValue(true);
        }

        if (needInvoice.getValue()) {
            createInvoice(order.getInvoice());
        } else {
            createInvoice(null);
        }

        binder.readBean(order);
        this.orderNumber.setText(isNew ? "" : order.getId().toString());
        this.customer.setText(order.getCustomer().getFullName());

        title.setVisible(isNew);
        metaContainer.setVisible(!isNew);
        //customerName.setValue(order.getCustomer().getFullName());
//          pricePlan.setValue(order.getPricePlan());

        if (order.getOrderState() != null) {
            getModel().setStatus(order.getOrderState().name());
        }
        if (order.getPricePlan() != null) {
            getModel().setPricePlan(new LocalDateToStringEncoder().encode(order.getPricePlan().getDate()));
        }

        currentOrder = order;
    }

    private void createInvoice(Invoice invoice) {
        invoiceContainer.add(invoiceEditor);
        invoiceEditor.addInvoiceDateListener(e -> invoiceDateChange(invoiceEditor, e.getDate()));
        invoiceEditor.addInvoiceNumberListener(e -> invoiceNumberChange(invoiceEditor, e.getNumber()));
        invoiceEditor.setValue(invoice);
    }


    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }


    public Binder<Order> getBinder() {
        return binder;
    }

    @Override
    public void setBinder(BeanValidationBinder<Order> binder) {
        binder.forField(status)
                .withValidator(new BeanValidator(Order.class, "orderState"))
                .bind(Order::getOrderState, (o, s) -> {
                    o.changeState(currentUser, s);
                });
        dueDate.setRequired(true);
        binder.bind(dueDate, "dueDate");
        payment.setItemLabelGenerator(createItemLabelGenerator(Payment::name));
        payment.setDataProvider(DataProvider.ofItems(Payment.values()));
        binder.bind(payment, "payment");
        payment.setRequired(true);

    }

    public Stream<HasValue<?, ?>> validate() {
        Stream<HasValue<?, ?>> errorFields = binder.validate().getFieldValidationErrors().stream()
                .map(BindingValidationStatus::getField);

        return errorFields /*Stream.concat(errorFields, itemsEditor.validate())*/;
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }
    public Registration addDeletePayEventListener(ComponentEventListener<PayDeleteEvent> listener) {
        return addListener(PayDeleteEvent.class, listener);
    }

    private void setTotalPrice(int totalPrice) {
        getModel().setTotalPrice(FormattingUtils.formatAsCurrency(totalPrice));
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    @Override
    public FormButtonsBar getButtons() {
        return null;
    }

    public boolean hasChanges() {
        return getBinder().hasChanges();     // itemEditor.hasChanges();
    }

    @Override
    public HasText getTitle() {
        return title;
    }

    public void clear() {
        binder.readBean(null);
        invoiceEditor.setValue(null);
        needInvoice.setValue(false);
        repaymentView.clear();
    }

    private void invoiceDateChange(InvoiceEditor editor, LocalDate value) {
        if (editor.getCurrentInvoice() == null) {
            Invoice invoice = new Invoice();
            editor.setCurrentInvoice(invoice);
            invoice.setDateInvoice(value);
            invoice.setNumberInvoice("");
            editor.setValue(invoice);
            editor.setHasChanges(true);
        }
    }

    private void invoiceNumberChange(InvoiceEditor editor, String value) {
        editor.setHasChanges(true);
        editor.getCurrentInvoice().setNumberInvoice(value);
        editor.setValue(editor.getCurrentInvoice());
        editor.setHasChanges(true);
    }

    public void setDefaultPrice(Price defaultPrice) {
        this.defaultPrice = defaultPrice;
        orderItemsView.setDefaultPrice(defaultPrice);
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }


    //Модель
    public interface Model extends TemplateModel {
        void setTotalPrice(String totalPrice);

        void setStatus(String status);

        void setPricePlan(String pricePlan);

        @Include({"id", "dueDate", "orderState", /*"pricePlan.date", */"items.price.name", "items.quantity",
                "items.totalPrice", "history.message", "history.createdBy.firstName", "history.timestamp"})
        @Encode(value = LongToStringEncoder.class, path = "id")
        @Encode(value = LocalDateToStringEncoder.class, path = "dueDate")
       /* @Encode(value = LocalDateToStringEncoder.class, path = "pricePlan.date")*/
        @Encode(value = OrderStateConverter.class, path = "orderState")
        @Encode(value = CurrencyFormatter.class, path = "items.totalPrice")
        @Encode(value = LocalDateTimeConverter.class, path = "history.timestamp")
        void setItem(Order order);
    }

//для экспорта спецификации
    private InputStream export(Order order) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = generateSpecFor(order);
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public JasperPrint generateSpecFor(Order order) throws IOException, JRException {
//        final JasperReport report = loadTemplate();
        final InputStream report = this.getClass().getResourceAsStream("/jasper/specification.jasper");
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(order.getItems());
        Map<String, Object> parameters = parameters(order, MarketConst.APP_LOCALE);
        parameters.put("itemDataSource", dataSource);
        return JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
    }

    private Map<String, Object> parameters(Order order, Locale locale) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("order", order);
        parameters.put("REPORT_LOCALE", locale);
        return parameters;
    }

    private final String invoice_template_path = "/jasper/specification.jrxml";

    private JasperReport loadTemplate() throws JRException {
        final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
        return JasperCompileManager.compileReport(jasperDesign);
    }

}
