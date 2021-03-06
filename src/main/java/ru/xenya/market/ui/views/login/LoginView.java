package ru.xenya.market.ui.views.login;


import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.templatemodel.TemplateModel;
import ru.xenya.market.ui.utils.MarketConst;

@Tag("login-view")
@HtmlImport("src/views/login/login-view.html")
@Route(value = "login")
@PageTitle("MM-Market")

@Viewport(MarketConst.VIEWPORT)
public class LoginView extends PolymerTemplate<LoginView.Model> implements PageConfigurator, AfterNavigationObserver {

    public LoginView() {
       // getElement().appendChild()
        getElement().addPropertyChangeListener("checkBoxChecked",
                e -> fireEvent(new FilterChanged(this, false)));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        boolean error = event.getLocation().getQueryParameters().getParameters().containsKey("error");
        getModel().setError(error);
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        // Force login page to use Shady DOM to avoid problems with browsers and
        // password managers not supporting shadow DOM
        //не правильно, так как не отображалось в Edge
//        settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
//                "window.customElements=window.customElements||{};" +
//                        "window.customElements.forcePolyfill=true;" +
//                        "window.ShadyDOM={force:true};", InitialPageSettings.WrapMode.JAVASCRIPT);

        settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
                "if(window.customElements) {" +
                        "window.customElements.forcePolyfill=true;" +
                        "window.ShadyDOM={force:true}};", InitialPageSettings.WrapMode.JAVASCRIPT);


    }

    public interface Model extends TemplateModel {
        void setError(boolean error);
    }

    public static class FilterChanged extends ComponentEvent<LoginView> {
        /**
         * Creates a new event using the given source and indicator whether the
         * event originated from the client side or the server side.
         *
         * @param source     the source component
         * @param fromClient <code>true</code> if the event originated from the client
         */
        public FilterChanged(LoginView source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}
