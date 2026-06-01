package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCow;
import tr.ozanbey.agricalc.webapp.service.service.animal.AnimalDashboardService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.util.List;

@Component("animalDashboardController")
@ViewScoped
@Getter
@Setter
public class AnimalDashboardController extends BaseController {

    @Autowired
    private AnimalDashboardService dashboardService;

    private List<UserDairyCow> userDairyCowList;

    @PostConstruct
    public void init() {
        userDairyCowList = dashboardService.getActiveFeedAsViewList(getCurrentUser().getUser().getId());
    }

}
