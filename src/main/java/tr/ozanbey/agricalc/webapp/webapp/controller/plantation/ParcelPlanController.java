package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcelPlan;
import tr.ozanbey.agricalc.webapp.service.service.plantation.ProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelPlanService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class ParcelPlanController extends BaseController {

    @Autowired
    private UserPlantParcelPlanService userPlantParcelPlanService;

    @Autowired
    private UserPlantParcelService plantParcelService;

    @Autowired
    private ProductService productService;
    private LocalDate planStartDate;

    private List<UserPlantParcelPlan> parcelPlanList;
    private Long parcelId;
    private UserPlantParcel userPlantParcel;

    @PostConstruct
    public void init() {
    }

    public void fillParcelList() throws IOException {
        if (parcelId == null || !checkParcelIdForUser(parcelId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }
        parcelPlanList = userPlantParcelPlanService.getParcelPlanList(parcelId);
    }

    public void setParcelId(Long parcelId) {
        if (Objects.equals(this.parcelId, parcelId)) {
            return;
        }
        this.parcelId = parcelId;
    }

    private boolean checkParcelIdForUser(Long parcelId) {
        Optional<UserPlantParcel> optionalUserPlantParcel = plantParcelService.getUserParcelByIdAndUserId(parcelId, getCurrentUser().getUser().getId());
        if (optionalUserPlantParcel.isPresent()) {
            userPlantParcel = optionalUserPlantParcel.get();
            return true;
        }
        return false;
    }


    public void addNewParcelPlan() throws IOException {
        userPlantParcelPlanService.createNewPlan(parcelId, planStartDate);
        fillParcelList();
    }
}
