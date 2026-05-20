package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumProductionType;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantAsset;
import tr.ozanbey.agricalc.webapp.service.service.FixedAssetService;
import tr.ozanbey.agricalc.webapp.webapp.view.UserPlantAssetView;

import java.util.List;
import java.util.Optional;

@Component("fixedAssetController")
@ViewScoped
@Getter
@Setter
public class FixedAssetController extends BaseController {

    @Autowired
    private FixedAssetService fixedAssetService;

    private EnumProductionType selectedProductionType;
    private EnumProductionType[] productionTypes = EnumProductionType.values();
    private EnumPlantAsset selectedAsset;
    private EnumPlantAsset[] enumPlantAssetList = EnumPlantAsset.values();
    private UserPlantAssetView.AssetDetail assetDetail = new UserPlantAssetView.AssetDetail();
    private List<UserPlantAssetView> plantAssetViewList;

    @PostConstruct
    public void init() {
    }

    public void onSelectProductionType() {
        if (selectedProductionType.equals(EnumProductionType.PLANT_PRODUCTION)) {
            plantAssetViewList = fixedAssetService.getPlantAssetByUserId(getCurrentUser().getUser().getId());
        }
    }

    public List<UserPlantAssetView.AssetDetail> detailListByEnum(EnumPlantAsset plantAsset) {
        return plantAssetViewList.stream().filter(pa -> pa.getPlantAsset().equals(plantAsset)).findFirst().get().getDetailList();
    }

    public void openDialog(EnumPlantAsset asset) {
        this.selectedAsset = asset;
    }

    public void cancelAsset() {
        assetDetail = new UserPlantAssetView.AssetDetail();
    }

    public void addAsset(EnumPlantAsset plantAsset) {
        Optional<UserPlantAssetView> plantAssetViewOptional = plantAssetViewList.stream().filter(pa -> pa.getPlantAsset().equals(plantAsset)).findFirst();
        if (plantAssetViewOptional.isPresent()) {
            plantAssetViewOptional.get().setQuantity(plantAssetViewOptional.get().getQuantity() + 1);
            plantAssetViewOptional.get().getDetailList().add(assetDetail);
        }
        assetDetail = new UserPlantAssetView.AssetDetail();
        PrimeFaces.current().ajax().update("fixedAssetForm:repeatDeneme:" + selectedAsset.getValue() + ":dataTable");
        selectedAsset = null;
    }

    public void removeAsset(EnumPlantAsset plantAsset) {
        Optional<UserPlantAssetView> plantAssetViewOptional = plantAssetViewList.stream().filter(pa -> pa.getPlantAsset().equals(plantAsset)).findFirst();
        if (plantAssetViewOptional.isPresent()) {
            plantAssetViewOptional.get().setQuantity(plantAssetViewOptional.get().getQuantity() - 1);
            plantAssetViewOptional.get().getDetailList().removeLast();
        }
        assetDetail = new UserPlantAssetView.AssetDetail();
        PrimeFaces.current().ajax().update("fixedAssetForm:repeatDeneme:" + plantAsset.getValue() + ":dataTable");
        selectedAsset = null;
    }

    public void updatePlantAsset() {
        fixedAssetService.savePlantAsset(plantAssetViewList, getCurrentUser().getUser());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Kayıt başarılı",
                        "Bitkisel demirbaş bilgileriniz güncellenmiştir."));
    }

}
