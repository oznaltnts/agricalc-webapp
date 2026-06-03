package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;
import tr.ozanbey.agricalc.webapp.webapp.controller.NavigationController;

import java.io.IOException;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowController extends BaseController {

    @Autowired
    private NavigationController navigationController;

    @Autowired
    private DairyCowService dairyCowService;

    private Long dairyCowId;

    public void redirectIfValidationFailed() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!context.isPostback() && context.isValidationFailed()) {
            navigationController.redirectToUrl("/secured/animal/dashboard");
        }
    }

}
