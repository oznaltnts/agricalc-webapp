package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumFarmerType;
import tr.ozanbey.agricalc.webapp.service.service.UserService;
import tr.ozanbey.agricalc.webapp.webapp.controller.plantation.PlantationController;
import tr.ozanbey.agricalc.webapp.webapp.view.FarmerProfileView;

@Component
@ViewScoped
@Getter
@Setter
public class FarmerProfileController extends PlantationController {

    @Autowired
    private UserService userService;

    private FarmerProfileView farmerView;
    private EnumFarmerType[] farmerTypes = EnumFarmerType.values();

    @PostConstruct
    public void init() {
        fillUserInfo();
    }

    private void fillUserInfo() {
        farmerView = new FarmerProfileView();
        //TODO from db
    }

}
