package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantationPlan;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationPlanService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserParcelService;
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
    private PlantationPlanService plantationPlanService;

    @Autowired
    private UserParcelService plantParcelService;

    @Autowired
    private PlantationProductService productService;

    private List<PlantationProduct> plantationProductList;
    private Long selectedProductId;
    private LocalDate planStartDate;

    private List<UserPlantationPlan> parcelPlanList;
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
        parcelPlanList = plantationPlanService.getParcelPlanList(parcelId);
    }

    public void fillProductList() {
        plantationProductList = productService.getActiveProducts(EnumStatus.ACTIVE);
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
        if (selectedProductId != null) {
            plantationPlanService.createNewPlan(parcelId, selectedProductId, planStartDate);
            fillParcelList();
        }
    }
}
