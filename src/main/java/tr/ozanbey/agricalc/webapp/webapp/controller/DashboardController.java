package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.User;

@Component
@ViewScoped
@Getter
@Setter
public class DashboardController extends BaseController {

    private User user;

    @PostConstruct
    public void init() {
        user = getCurrentUser().getUser();
    }

}
