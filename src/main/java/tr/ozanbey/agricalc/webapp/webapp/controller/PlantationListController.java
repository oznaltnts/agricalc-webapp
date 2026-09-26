package tr.ozanbey.agricalc.webapp.webapp.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.CityDistrict;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationType;
import tr.ozanbey.agricalc.webapp.service.service.CityService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.ProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantationService;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.plantation.PlantationView;

import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class PlantationListController extends BaseController {

    private final UserPlantationService userPlantationService;
    private final CityService cityService;
    private final ProductService productService;

    public PlantationListController(UserPlantationService userPlantationService,
                                    CityService cityService,
                                    ProductService productService) {
        this.userPlantationService = userPlantationService;
        this.cityService = cityService;
        this.productService = productService;
    }

    private PlantationView selectedPlantationView;
    private List<PlantationView> plantationList;
    private EnumStatus[] statuses = EnumStatus.userStatuses();
    private EnumPlantationType[] plantationTypes = EnumPlantationType.values();
    private List<City> cityList;
    private List<CityDistrict> districtList;
    private List<PlantationProduct> productList;

    @PostConstruct
    public void init() {
        cityList = cityService.getAllCities();
        productList = productService.getProductListByPlantationTypeAndStatus(
                EnumPlantationType.ORCHARD, EnumStatus.ACTIVE);
        fillPlantationList();
    }

    private void fillPlantationList() {
        selectedPlantationView = null;
        plantationList = userPlantationService.getPlantationViewList(getCurrentUser().getUser().getId(), EnumStatus.userStatuses());
    }

    public void create() {
        this.selectedPlantationView = new PlantationView();
    }

    public void update(PlantationView selectedPlantationView) {
        this.selectedPlantationView = new PlantationView(selectedPlantationView);
        handleTypeSelect();
        handleCitySelect();
    }

    public void delete(PlantationView selectedPlantationView) {
        userPlantationService.deletePlantationDetail(selectedPlantationView, getCurrentUser().getUser());
        fillPlantationList();
        JSFUtils.addInfoMessage(null, "Success", "Data deleted");
    }

    public void handleTypeSelect() {
        if (EnumPlantationType.ORCHARD.equals(selectedPlantationView.getSelectedType())) {
            Optional<PlantationProduct> productOptional = productList.stream()
                    .filter(p -> p.getId().equals(selectedPlantationView.getSelectedPrimaryProductId()))
                    .findAny();
            if (productOptional.isEmpty()) {
                selectedPlantationView.setSelectedPrimaryProductId(null);
            }
        } else
            selectedPlantationView.setSelectedPrimaryProductId(null);
    }

    public void handleCitySelect() {
        districtList = cityService.getDistrictListByCityId(selectedPlantationView.getSelectedCityId());
    }

    public void handleClose() {
        selectedPlantationView = null;
        districtList = null;
    }

    public void savePlantationDetail() {
        if (selectedPlantationView.getOwnedOrRental() == null) {
            selectedPlantationView.setSellOrRentPrice(null);
        }
        userPlantationService.savePlantationDetail(selectedPlantationView, getCurrentUser().getUser());
        fillPlantationList();
        PrimeFaces.current().executeScript("PF('sidebarWidgetVar').hide()");
    }

}
