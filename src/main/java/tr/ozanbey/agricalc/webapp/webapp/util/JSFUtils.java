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

    public static void addInfoMsg(String msg, String component) {
        addMsg(msg, FacesMessage.SEVERITY_INFO, component);
    }

    public static void addErrorMsg(String msg, String component) {
        addMsg(msg, FacesMessage.SEVERITY_ERROR, component);
    }

    public static void addWarnMsg(String msg, String component) {
        addMsg(msg, FacesMessage.SEVERITY_WARN, component);
    }

    private static void addMsg(String msg, FacesMessage.Severity severity, String component) {
        FacesContext.getCurrentInstance().addMessage(component, new FacesMessage(severity, msg, ""));
    }

    public static void putSessionMapParam(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
    }

    public static void removeSessionMapParam(String key) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
    }

    public static Object getSessionMapParam(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
    }
}
