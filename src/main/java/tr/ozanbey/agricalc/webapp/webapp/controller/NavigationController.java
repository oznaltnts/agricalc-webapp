package tr.ozanbey.agricalc.webapp.webapp.controller;

import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("navigationController")
public class NavigationController {

    public void redirectToError() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/common/error");
    }

    public void redirectToUrl(String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    public void redirectToHome() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/");
    }
}
