package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelDetail;
import tr.ozanbey.agricalc.webapp.service.service.CityService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelService;

import java.util.List;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class ParcelProfileController extends PlantationController {

    @Autowired
    private UserPlantParcelService userPlantParcelService;

    @Autowired
    private PlantationProductService productService;

    @Autowired
    private CityService cityService;

    private Long selectedParcelId;
    private List<PlantationProduct> plantationProductList;
    private Long selectedProductId;
    private String parcelName;
    private Double areaDecare;
    private List<City> cityList;
    private Long selectedCityId;

    private List<UserPlantParcel> plantParcelList;
    private EnumParcelDetail[] parcelDetails = EnumParcelDetail.values();

    @PostConstruct
    public void init() {
        fillParcelList();
    }

    private void fillParcelList() {
        plantParcelList = userPlantParcelService.getPlantParcelList(getCurrentUser().getUser().getId());
    }

    public void fillProductCityList() {
        if (plantationProductList == null || plantationProductList.isEmpty() || cityList == null || cityList.isEmpty()) {
            plantationProductList = productService.getActiveProducts(EnumStatus.ACTIVE);
            cityList = cityService.getAllCities();
        }
    }

    public void saveNewParcel() {
        if (parcelName != null) {
            Optional<UserPlantParcel> optionalView = plantParcelList.stream()
                    .filter(v ->
                            v.getParcelName().equals(parcelName))
                    .findFirst();
            if (optionalView.isEmpty()) {
                userPlantParcelService.saveParcel(selectedParcelId, selectedProductId, parcelName, areaDecare, selectedCityId, getCurrentUser().getUser());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Kayıt başarılı", "Parseliniz listenize eklenmiştir."));
                selectedParcelId = null;
                selectedProductId = null;
                parcelName = null;
                areaDecare = null;
                selectedCityId = null;
                fillParcelList();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Kayıt başarısız", "Bu isimli parseliniz zaten mevcut."));
            }
        }
    }
}
