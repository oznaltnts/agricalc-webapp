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
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelDetail;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelType;
import tr.ozanbey.agricalc.webapp.service.service.PlantParcelService;
import tr.ozanbey.agricalc.webapp.webapp.view.ParcelInformationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("plantParcelController")
@ViewScoped
@Getter
@Setter
public class PlantParcelController extends BaseController {

    @Autowired
    private PlantParcelService plantParcelService;

    private EnumParcelType[] parcelTypes = EnumParcelType.values();
    private EnumParcelType selectedParcelType;
    private List<ParcelInformationView> parcelViewList = new ArrayList<>();
    private ParcelInformationView selectedParcelView;

    private EnumParcelDetail[] parcelDetails = EnumParcelDetail.values();

    @PostConstruct
    public void init() {
        selectedParcelType = EnumParcelType.OPEN_FIELD;
        fillParcelViewList();
    }

    private void fillParcelViewList() {
        parcelViewList = plantParcelService.parcelListByUser(getCurrentUser().getUser().getId());
    }

    public void onTabChange(TabChangeEvent event) {
        selectedParcelView = null;
        selectedParcelType = (EnumParcelType) event.getData();
    }

    public List<ParcelInformationView> detailListByEnum(EnumParcelType parcelType) {
        return parcelViewList.stream()
                .filter(pa -> pa.getParcelType().equals(parcelType))
                .toList();
    }

    public void editRow(ParcelInformationView parcelView) {
        selectedParcelView = parcelView;
    }

    public void addNew() {
        selectedParcelView = new ParcelInformationView();
        selectedParcelView.setParcelType(selectedParcelType);
    }

    public void cancelSaveButton() {
        selectedParcelView = null;
    }

    public void saveButton() {
        saveParcelView();
    }

    private void saveParcelView() {
        if (selectedParcelView.getParcelName() != null) {
            Optional<ParcelInformationView> optionalView = parcelViewList.stream()
                    .filter(v ->
                            v.getParcelName().equals(selectedParcelView.getParcelName()) &&
                                    !v.getRecordId().equals(selectedParcelView.getRecordId()))
                    .findFirst();
            if (optionalView.isEmpty()) {
                plantParcelService.saveParcel(selectedParcelView, getCurrentUser().getUser());
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Kayıt başarılı", "Parseliniz listenize eklenmiştir."));
                selectedParcelView = null;
                fillParcelViewList();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Kayıt başarısız", "Bu isimli parseliniz zaten mevcut."));
            }
        }
    }

    public void deleteRow(ParcelInformationView parcelView) {
        plantParcelService.deleteParcel(parcelView.getRecordId());
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Silme başarılı", "Parseliniz listenizden çıkartılmıştır."));
        fillParcelViewList();
    }

}
