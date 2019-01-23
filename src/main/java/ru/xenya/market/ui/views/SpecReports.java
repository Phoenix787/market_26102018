package ru.xenya.market.ui.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.vaadin.alejandro.PdfBrowserViewer;
import org.vaadin.reports.PrintPreviewReport;
import ru.xenya.market.backend.data.entity.Order;
import ru.xenya.market.backend.data.entity.OrderSummary;
import ru.xenya.market.backend.service.OrderService;
import ru.xenya.market.ui.utils.MarketConst;

import java.io.*;
import java.util.*;

//@Route("report")
//@BodySize(height = "100vh", width = "100vw")
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SpecReports extends Div  {
    private final String invoice_template_path = "/jasper/specification.jrxml";

   // private final OrderService orderService;

    private Order currentOrder;

    public SpecReports(/*OrderService orderService*/ Order order) {
        currentOrder = order;
        PdfBrowserViewer pdfBrowserViewer = new PdfBrowserViewer(getStreamResource());
        pdfBrowserViewer.setHeight("100%");
        add(pdfBrowserViewer);
        setHeight("100%");

//        this.orderService = orderService;

//        PrintPreviewReport<Order> report = new PrintPreviewReport<>(Order.class);
//        report.setItems(Collections.singletonList(orderService.findById(3971L)));
//        add(report);

        //todo здесь настраиваем вид отчета

//        StreamResource streamResource = report.getStreamResource("report.pdf",
//                (SerializableSupplier<List<? extends Order>>) orderService.findAll(),
//                PrintPreviewReport.Format.PDF);
    }

    private StreamResource getStreamResource() {
        return new StreamResource("spec.pdf", () -> export(getCurrentOrder()));
//        return new StreamResource("spec.pdf", () -> getClass().getResourceAsStream("/spec.pdf"));
    }

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


    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public void generateInvoiceFor(Order order, Locale locale) throws IOException {

        File pdfFile = File.createTempFile("my-invoice", ".pdf");
        String path = pdfFile.getPath();

        try (FileOutputStream pos = new FileOutputStream(pdfFile)) {

            final JasperReport report = loadTemplate();
            final Map<String, Object> parameters = parameters(order, MarketConst.APP_LOCALE);
            final JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(Collections.singletonList("Invoice"));

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, path);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public JasperPrint generateSpecFor(Order order) throws IOException, JRException {
       final InputStream jReport = this.getClass().getResourceAsStream("/jasper/specification.jasper");
        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList("Specification"));
        Map<String, Object> parameters = parameters(order, MarketConst.APP_LOCALE);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jReport, parameters, dataSource);
        return jasperPrint;
    }

    private Map<String, Object> parameters(Order order, Locale locale) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("order", order);
        parameters.put("REPORT_LOCALE", locale);
        return parameters;
    }

    private JasperReport loadTemplate() throws JRException {
        final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
        return JasperCompileManager.compileReport(jasperDesign);
    }

    public void open() {
//        PdfBrowserViewer pdfBrowserViewer = new PdfBrowserViewer(getStreamResource());
//        pdfBrowserViewer.setHeight("100%");
//        pdfBrowserViewer.setWidth("100%");
//        add(pdfBrowserViewer);
//        setHeight("100%");
    }


}
