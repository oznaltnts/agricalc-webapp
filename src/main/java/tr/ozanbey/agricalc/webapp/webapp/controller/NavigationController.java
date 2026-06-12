package tr.ozanbey.agricalc.webapp.webapp.controller;

import jakarta.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NavigationController {

    public void redirectToError() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/common/error");
    }

    public void redirectToHome() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/");
    }

    public void redirectToUrl(String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    public void redirectToLoginWithDuration(int duration) {
        PrimeFaces.current().executeScript("setTimeout(() => {window.location.href = '/login';}, " + duration + ");");
    }

}
