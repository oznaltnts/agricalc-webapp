package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumProductionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantAsset;
import tr.ozanbey.agricalc.webapp.service.service.FixedAssetService;
import tr.ozanbey.agricalc.webapp.service.service.HomeService;
import tr.ozanbey.agricalc.webapp.service.service.UserService;
import tr.ozanbey.agricalc.webapp.webapp.view.UserInformationView;
import tr.ozanbey.agricalc.webapp.webapp.view.UserPlantAssetView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("profileController")
@ViewScoped
@Getter
@Setter
public class ProfileController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private FixedAssetService fixedAssetService;

    @Autowired
    private HomeService homeService;

    private List<City> cityList;
    private UserInformationView informationView;

    @PostConstruct
    public void init() {
        cityList = homeService.getAllCities();
        informationView = userService.getInformationByUserId();
    }

    private void updateUserProfile() {
        if (userService.save(informationView)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Kayıt başarılı",
                            "Kullanıcı bilgileriniz güncellenmiştir."));
        }
    }

    private void updatePlantAsset() {
        fixedAssetService.savePlantAsset(plantAssetViewList);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Kayıt başarılı",
                        "Bitkisel demirbaş bilgileriniz güncellenmiştir."));
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getOldStep().equals("tab1")) {
            updateUserProfile();
        } else if (event.getOldStep().equals("tab2")) {
            updatePlantAsset();
        }
        return event.getNewStep();
    }

    private EnumProductionType selectedProductionType;
    private EnumProductionType[] productionTypes = EnumProductionType.values();

    private UserPlantAssetView.AssetDetail assetDetail = new UserPlantAssetView.AssetDetail();

    public void addAsset(EnumPlantAsset plantAsset) {
        Optional<UserPlantAssetView> plantAssetViewOptional = plantAssetViewList.stream().filter(pa -> pa.getPlantAsset().equals(plantAsset)).findFirst();
        if (plantAssetViewOptional.isPresent()) {
            plantAssetViewOptional.get().setQuantity(plantAssetViewOptional.get().getQuantity() + 1);
            plantAssetViewOptional.get().getDetailList().add(assetDetail);
        }
        assetDetail = new UserPlantAssetView.AssetDetail();
        PrimeFaces.current().ajax().update("profileForm:repeatDeneme:" + selectedAsset.getValue() + ":repeatPanel");
        selectedAsset = null;
    }

    public void removeAsset(EnumPlantAsset plantAsset) {
        Optional<UserPlantAssetView> plantAssetViewOptional = plantAssetViewList.stream().filter(pa -> pa.getPlantAsset().equals(plantAsset)).findFirst();
        if (plantAssetViewOptional.isPresent()) {
            plantAssetViewOptional.get().setQuantity(plantAssetViewOptional.get().getQuantity() - 1);
            plantAssetViewOptional.get().getDetailList().removeLast();
        }
        assetDetail = new UserPlantAssetView.AssetDetail();
        PrimeFaces.current().ajax().update("profileForm:repeatDeneme:" + plantAsset.getValue() + ":repeatPanel");
        selectedAsset = null;
    }

    public void cancelAsset() {
        assetDetail = new UserPlantAssetView.AssetDetail();
    }

    private List<UserPlantAssetView> plantAssetViewList = new ArrayList<>();

    public void onSelectProductionType() {
        if (selectedProductionType.equals(EnumProductionType.PLANT_PRODUCTION)) {
            plantAssetViewList = fixedAssetService.getPlantAssetByUserId();
        }
    }

    public List<UserPlantAssetView.AssetDetail> detailListByEnum(EnumPlantAsset plantAsset) {
        return plantAssetViewList.stream().filter(pa -> pa.getPlantAsset().equals(plantAsset)).findFirst().get().getDetailList();
    }

    private EnumPlantAsset[] enumPlantAssetList = EnumPlantAsset.values();

    private EnumPlantAsset selectedAsset;

    public void openDialog(EnumPlantAsset asset) {
        this.selectedAsset = asset;
    }

    public void saveAll() {
        updateUserProfile();
        updatePlantAsset();
    }
}
