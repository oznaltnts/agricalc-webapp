package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component("animalAssetController")
@ViewScoped
@Getter
@Setter
public class AnimalAssetController extends BaseController {

    @PostConstruct
    public void init() {
    }

}
