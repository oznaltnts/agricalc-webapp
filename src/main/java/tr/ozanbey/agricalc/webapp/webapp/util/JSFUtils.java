package tr.ozanbey.agricalc.webapp.webapp.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.webapp.controller.LocaleController;
import tr.ozanbey.agricalc.webapp.webapp.util.context.SpringContextAccessor;

import java.util.ResourceBundle;

@Component
public class JSFUtils {

    public static String getLocaleMessage(String key) {
        LocaleController localeController = SpringContextAccessor.getBean(LocaleController.class);
        return ResourceBundle.getBundle("messages", localeController.getLocale()).getString(key);
    }

    public static void addInfoMessage(String component, String summary, String detail) {
        addMessage(component, FacesMessage.SEVERITY_INFO, summary, detail);
    }

    public static void addErrorMessage(String component, String summary, String detail) {
        addMessage(component, FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    public static void addWarnMessage(String component, String summary, String detail) {
        addMessage(component, FacesMessage.SEVERITY_WARN, summary, detail);
    }

    public static void addFatalMessage(String component, String summary, String detail) {
        addMessage(component, FacesMessage.SEVERITY_FATAL, summary, detail);
    }

    private static void addMessage(String component, FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(component, new FacesMessage(severity, summary, detail));
    }

}
