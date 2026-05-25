package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantAsset;
import tr.ozanbey.agricalc.webapp.service.service.PlantAssetService;
import tr.ozanbey.agricalc.webapp.webapp.view.UserPlantAssetView;

import java.util.List;
import java.util.Optional;

@Component("plantAssetController")
@ViewScoped
@Getter
@Setter
public class PlantAssetController extends BaseController {

    @Autowired
    private PlantAssetService plantAssetService;

    private EnumPlantAsset[] plantAssetTypes = EnumPlantAsset.values();
    private EnumPlantAsset selectedAssetType;
    private List<UserPlantAssetView> plantAssetViewList;
    private UserPlantAssetView.AssetDetail selectedAssetDetail;

    @PostConstruct
    public void init() {
        selectedAssetType = EnumPlantAsset.TRACTOR;
        plantAssetViewList = plantAssetService.getPlantAssetByUserId(getCurrentUser().getUser().getId());
    }

    public void onTabChange(TabChangeEvent event) {
        selectedAssetDetail = null;
        selectedAssetType = (EnumPlantAsset) event.getData();
    }

    public List<UserPlantAssetView.AssetDetail> detailListByEnum(EnumPlantAsset plantAsset) {
        return plantAssetViewList.stream()
                .filter(pa -> pa.getPlantAsset().equals(plantAsset))
                .findFirst()
                .get()
                .getDetailList();
    }

    public void editRow(UserPlantAssetView.AssetDetail assetDetail) {
        selectedAssetDetail = assetDetail;
    }

    public void addNew() {
        selectedAssetDetail = new UserPlantAssetView.AssetDetail();
    }

    public void cancelSaveButton() {
        selectedAssetDetail = null;
    }

    public void saveButton() {
        addAsset();
    }

    private void addAsset() {
        Optional<UserPlantAssetView> assetView = plantAssetViewList.stream()
                .filter(pa -> pa.getPlantAsset().equals(selectedAssetType))
                .findFirst();
        if (assetView.isPresent()) {
            Optional<UserPlantAssetView.AssetDetail> optionalAsset = assetView.get().getDetailList().stream()
                    .filter(d -> selectedAssetDetail.getRecordId().equals(d.getRecordId()))
                    .findFirst();
            if (optionalAsset.isEmpty()) {
                assetView.get().getDetailList().add(selectedAssetDetail);
            }
            updatePlantAsset(assetView.get());
        }
        selectedAssetDetail = null;
    }

    public void deleteRow(UserPlantAssetView.AssetDetail assetDetail) {
        Optional<UserPlantAssetView> assetView = plantAssetViewList.stream()
                .filter(pa -> pa.getPlantAsset().equals(selectedAssetType))
                .findFirst();
        if (assetView.isPresent()) {
            Optional<UserPlantAssetView.AssetDetail> optionalAsset = assetView.get().getDetailList().stream()
                    .filter(d -> d.getRecordId().equals(assetDetail.getRecordId()))
                    .findFirst();
            if (optionalAsset.isPresent()) {
                assetView.get().getDetailList().remove(assetDetail);
            }
            updatePlantAsset(assetView.get());
            selectedAssetDetail = null;
        }
    }

    private void updatePlantAsset(UserPlantAssetView view) {
        plantAssetService.savePlantAsset(view, getCurrentUser().getUser());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Kayıt başarılı", "Bitkisel demirbaş bilgileriniz güncellenmiştir."));
    }

}
