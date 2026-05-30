package tr.ozanbey.agricalc.webapp.webapp.controller.animal;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.animal.DairyCowService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;
import tr.ozanbey.agricalc.webapp.webapp.view.DairyCowView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component("dairyCowController")
@ViewScoped
@Getter
@Setter
public class DairyCowController extends BaseController {

    @Autowired
    private DairyCowService dairyCowService;

    private LocalDateTime minDate;
    private LocalDateTime maxDate;
    private List<DairyCowView> selectedFeedList;
    private List<DairyCowView> dairyCowViewList = new ArrayList<>();

    @PostConstruct
    public void init() {
        minDate = LocalDateTime.now().minusYears(1);
        maxDate = LocalDateTime.now().plusYears(1);
        dairyCowViewList = dairyCowService.getActiveFeedAsViewList();
    }

}
