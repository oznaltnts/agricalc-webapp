package tr.ozanbey.agricalc.webapp.webapp.controller.admin;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

@Component("userManagementController")
@ViewScoped
@Getter
@Setter
public class UserManagementController extends BaseController {

    @PostConstruct
    public void init() {
    }

}
