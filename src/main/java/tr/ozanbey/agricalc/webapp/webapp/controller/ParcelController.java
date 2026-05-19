package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelDetail;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelType;
import tr.ozanbey.agricalc.webapp.service.service.ParcelService;
import tr.ozanbey.agricalc.webapp.webapp.view.ParcelInformationView;

@Component("parcelController")
@ViewScoped
@Getter
@Setter
public class ParcelController extends BaseController {

    @Autowired
    private ParcelService parcelService;

    private ParcelInformationView parcelView = new ParcelInformationView();
    private EnumParcelDetail[] parcelDetails = EnumParcelDetail.values();
    private EnumParcelType[] parcelTypes = EnumParcelType.values();

    @PostConstruct
    public void init() {
    }

    public void onTabChange(TabChangeEvent event) {
    }

    public void save() {
    }

}
