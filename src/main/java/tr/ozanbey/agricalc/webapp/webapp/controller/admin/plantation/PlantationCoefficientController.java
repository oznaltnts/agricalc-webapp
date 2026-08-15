package tr.ozanbey.agricalc.webapp.webapp.controller.admin.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationCoefficient;
import tr.ozanbey.agricalc.webapp.service.service.admin.AdminService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class PlantationCoefficientController extends BaseController {

    @Autowired
    private AdminService adminService;

    private List<PlantationCoefficient> coefficientList;

    @PostConstruct
    public void init() {
        coefficientList = adminService.getPlantationCoefficientList();
    }

}
