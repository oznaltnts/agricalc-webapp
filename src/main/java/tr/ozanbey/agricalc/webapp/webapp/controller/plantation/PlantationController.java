package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;
import tr.ozanbey.agricalc.webapp.webapp.controller.NavigationController;

@Component
@ViewScoped
@Getter
@Setter
public class PlantationController extends BaseController {

    @Autowired
    private NavigationController navigationController;

}
