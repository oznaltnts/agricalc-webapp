package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationType;
import tr.ozanbey.agricalc.webapp.service.service.plantation.ProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantationService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlantationPlanView;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlantationView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
@ViewScoped
@Getter
@Setter
public class PlantationPlanListController extends BaseController {

    private final UserPlantationService userPlantationService;
    private final ProductService productService;

    public PlantationPlanListController(UserPlantationService userPlantationService,
                                        ProductService productService) {
        this.userPlantationService = userPlantationService;
        this.productService = productService;
    }

    private Long userPlantationId;
    private PlantationView selectedPlantationView;
    private PlantationPlanView selectedPlantationPlanView;
    private List<PlantationPlanView> plantationPlanList;
    private EnumStatus[] statuses = EnumStatus.userStatuses();
    private List<PlantationProduct> productList;
    private List<PlantationProduct> openFieldProductList;

    @PostConstruct
    public void init() {
        openFieldProductList = productService.getProductListByPlantationTypeAndStatus(
                EnumPlantationType.OPEN_FIELD, EnumStatus.ACTIVE);
    }

    public void setUserPlantationId(Long userPlantationId) {
        if (Objects.equals(this.userPlantationId, userPlantationId)) {
            return;
        }
        this.userPlantationId = userPlantationId;
    }

    public void fillPlantationPlanList() throws IOException {
        if (!checkPlantationIdForUser(userPlantationId)) {
            super.navigationController.redirectToUrl("/secured/plantation-list");
            return;
        }
        loadListFromDB();
    }

    private boolean checkPlantationIdForUser(Long userPlantationId) {
        if (userPlantationId == null)
            return false;
        PlantationView optionalView = userPlantationService.getPlantationViewByIdAndUserId(userPlantationId, getCurrentUser().getUser().getId());
        if (optionalView != null) {
            selectedPlantationView = optionalView;
            if (EnumPlantationType.ORCHARD.equals(selectedPlantationView.getSelectedType()))
                productList = productService.getProductListByPlantationTypeAndStatus(selectedPlantationView.getSelectedType(), EnumStatus.ACTIVE);
            else
                productList = productService.getProductListByStatus(EnumStatus.ACTIVE);
            return true;
        }
        return false;
    }

    private void loadListFromDB() {
        selectedPlantationPlanView = null;
        plantationPlanList = userPlantationService.getPlantationPlanViewList(userPlantationId, EnumStatus.userStatuses());
    }

    public void create() {
        this.selectedPlantationPlanView = new PlantationPlanView();
        this.selectedPlantationPlanView.setSelectedPrimaryProductId(selectedPlantationView.getSelectedPrimaryProductId());
        this.selectedPlantationPlanView.setSelectedPrimaryProductName(selectedPlantationView.getSelectedPrimaryProductName());
    }

    public void update(PlantationPlanView selectedPlantationPlanView) {
        this.selectedPlantationPlanView = new PlantationPlanView(selectedPlantationPlanView);
    }

    public void delete(PlantationPlanView selectedPlantationPlanView) throws IOException {
        userPlantationService.deletePlantationPlanDetail(selectedPlantationPlanView, userPlantationId);
        loadListFromDB();
        JSFUtils.addInfoMessage(null, "Success", "Data deleted");
    }

    public void handlePrimaryProductSelect() {
        Optional<PlantationProduct> productOptional = productList.stream()
                .filter(p -> p.getId().equals(selectedPlantationPlanView.getSelectedPrimaryProductId()))
                .findAny();
        if (productOptional.isPresent()) {
            selectedPlantationPlanView.setSelectedPlantationType(productOptional.get().getPlantationType());
            selectedPlantationPlanView.setProcessTooltipMessage("Süreç başladıktan sonra değiştirilemez.");
            if (productOptional.get().getPlantationType().equals(EnumPlantationType.ORCHARD)) {
                selectedPlantationPlanView.setProcessTooltipMessage("Bahçe tipi (Çok yıllık) ürünler değiştirilemez.");
                selectedPlantationPlanView.setSelectedSecondaryProductId(null);
                selectedPlantationPlanView.setSelectedSecondaryProductName(null);
                selectedPlantationPlanView.setSelectedLastProductId(null);
                selectedPlantationPlanView.setSelectedLastProductName(null);
            }
        }
    }

    public void handleClose() {
        selectedPlantationPlanView = null;
        JSFUtils.addWarnMessage(null, "Sidebar Status", "Closed");
    }

    public void savePlantationPlanDetail() throws IOException {
        userPlantationService.savePlantationPlanDetail(selectedPlantationPlanView, userPlantationId);
        loadListFromDB();
        PrimeFaces.current().executeScript("PF('sidebarWidgetVar').hide()");
    }
}
