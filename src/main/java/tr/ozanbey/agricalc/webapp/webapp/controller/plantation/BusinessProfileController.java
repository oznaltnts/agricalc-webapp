package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.webapp.view.BusinessProfileView;

@Component
@ViewScoped
@Getter
@Setter
public class BusinessProfileController extends PlantationController {

    private BusinessProfileView businessView;

    @PostConstruct
    public void init() {
        fillUserInfo();
    }

    private void fillUserInfo() {
        businessView = new BusinessProfileView();
        //TODO from db
    }

}
