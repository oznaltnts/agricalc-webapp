package tr.ozanbey.agricalc.webapp.webapp.controller.animal.dairycow;


import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.service.animal.dairycow.DairyCowService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;
import tr.ozanbey.agricalc.webapp.webapp.controller.NavigationController;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class DairyCowController extends BaseController {

    @Autowired
    private NavigationController navigationController;

    @Autowired
    private DairyCowService dairyCowService;

    private Long barnId;
    private UserDairyCowBarn userDairyCowBarn;

    public void setBarnId(Long barnId) throws IOException {
        if (Objects.equals(this.barnId, barnId)) {
            return;
        }

        if (barnId == null || !checkBarnIdForUser(barnId)) {
            navigationController.redirectToUrl("/secured/animal/dairy-cow/barn");
            return;
        }
        this.barnId = barnId;
    }

    private boolean checkBarnIdForUser(Long barnId) {
        Optional<UserDairyCowBarn> optionalUserDairyCow = dairyCowService.getUserBarnByIdAndUserId(barnId, getCurrentUser().getUser().getId());
        if (optionalUserDairyCow.isPresent()) {
            boolean returnValue = Objects.equals(optionalUserDairyCow.get().getUser().getId(), getCurrentUser().getUser().getId());
            if (returnValue) {
                this.userDairyCowBarn = optionalUserDairyCow.get();
            }
            return returnValue;
        }
        return false;
    }

}
