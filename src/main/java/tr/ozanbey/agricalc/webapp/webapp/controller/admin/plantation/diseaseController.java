package tr.ozanbey.agricalc.webapp.webapp.controller.admin.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationDisease;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumDiseaseType;
import tr.ozanbey.agricalc.webapp.service.service.admin.DiseaseMedicineService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;
import tr.ozanbey.agricalc.webapp.webapp.util.JSFUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.admin.DiseaseView;
import tr.ozanbey.agricalc.webapp.webapp.view.admin.ProductQuestionDiseaseView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@ViewScoped
@Getter
@Setter
public class diseaseController extends BaseController {

    private DiseaseView selectedDiseaseView;
    private List<DiseaseView> diseaseViewList;
    private EnumStatus[] statuses = EnumStatus.values();
    private EnumDiseaseType[] types = EnumDiseaseType.values();

    @Autowired
    private DiseaseMedicineService diseaseService;

    @PostConstruct
    public void init() {
        loadDiseaseList();
    }

    private void loadDiseaseList() {
        diseaseViewList = diseaseService.getAllAsDiseaseViewList();
    }

    public void onRowSelect(SelectEvent<DiseaseView> event) {
        if (event != null) {
            selectedDiseaseView = event.getObject();
        } else
            selectedDiseaseView = new DiseaseView(new PlantationDisease());
    }

    public void saveSelectedDiseaseView() {
        if (checkRequired()) {
            if (checkIsThereChange()) {
                if (checkValidations()) {
                    diseaseService.saveSelectedDiseaseView(selectedDiseaseView.getDiseaseId(),
                            selectedDiseaseView.getSelectedStatus(), selectedDiseaseView.getSelectedDiseaseType(), selectedDiseaseView.getSelectedDiseaseName(),
                            selectedDiseaseView.getSelectedProductQuestionDiseaseViewList());
                    setSelectedDiseaseView(null);
                    loadDiseaseList();
                    JSFUtils.addInfoMessage(null, "Kayıt başarılı", "Hastalık listesi güncellenmiştir.");
                    PrimeFaces.current().executeScript("PF('addDialogWidgetVar').hide()");
                } else {
                    JSFUtils.addErrorMessage(null, "Kayıt başarısız", "Bu hastalık tipi ve hastalık adı zaten kayıtlı.");
                }
            } else {
                JSFUtils.addErrorMessage(null, "Kayıt başarısız", "Değişiklik bulunamadı.");
            }
        } else {
            JSFUtils.addErrorMessage(null, "Kayıt başarısız", "* işaretli olan zorunlu girişleri yapın.");
        }
    }

    private boolean checkValidations() {
        Optional<DiseaseView> optional = diseaseViewList.stream().filter(d ->
                        d.getDisease().getType().equals(selectedDiseaseView.getSelectedDiseaseType()) &&
                                d.getDisease().getName().equalsIgnoreCase(selectedDiseaseView.getSelectedDiseaseName()))
                .findFirst();
        return optional.isEmpty() || optional.get().getDisease().getId().equals(selectedDiseaseView.getDiseaseId());
    }

    private boolean checkRequired() {
        return selectedDiseaseView != null && selectedDiseaseView.getSelectedDiseaseType() != null && selectedDiseaseView.getSelectedDiseaseName() != null;
    }

    private boolean checkIsThereChange() {
        for (ProductQuestionDiseaseView view : selectedDiseaseView.getSelectedProductQuestionDiseaseViewList()) {
            if (!view.getInputAmount().equals(view.getProductQuestionDisease().getWaterAmount())
                    || !view.getInputPrice().equals(view.getProductQuestionDisease().getAdhesivePrice())) {
                return true;
            }
        }
        return !selectedDiseaseView.getSelectedStatus().equals(selectedDiseaseView.getDisease().getStatus())
                || !selectedDiseaseView.getSelectedDiseaseType().equals(selectedDiseaseView.getDisease().getType())
                || !selectedDiseaseView.getSelectedDiseaseName().equals(selectedDiseaseView.getDisease().getName());
    }

    public void handleProductListOnSelect() {
        if (selectedDiseaseView.getSelectedDiseaseType() == null) {
            selectedDiseaseView.setSelectedProductQuestionDiseaseViewList(null);
            selectedDiseaseView.setProductQuestionDiseaseViewList(null);
        } else {
            List<ProductQuestionDiseaseView> productQuestionDiseaseViewList = diseaseService.getProductListByQuestionId(selectedDiseaseView.getSelectedDiseaseType());
            selectedDiseaseView.setSelectedProductQuestionDiseaseViewList(productQuestionDiseaseViewList.stream().filter(vi -> vi.getProductQuestionDiseaseId() != null).collect(Collectors.toList()));
            selectedDiseaseView.setProductQuestionDiseaseViewList(productQuestionDiseaseViewList);
        }
    }
}
