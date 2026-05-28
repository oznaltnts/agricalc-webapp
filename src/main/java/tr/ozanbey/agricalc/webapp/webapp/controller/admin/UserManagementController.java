package tr.ozanbey.agricalc.webapp.webapp.controller.admin;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.service.UserService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;
import tr.ozanbey.agricalc.webapp.webapp.view.UserInformationView;

import java.util.List;

@Component("userManagementController")
@ViewScoped
@Getter
@Setter
public class UserManagementController extends BaseController {

    @Autowired
    private UserService userService;

    private List<UserInformationView> userInfoList;
    private User selectedUser;

    @PostConstruct
    public void init() {
        userInfoList = userService.getUsersAsInfoViewList();
    }

    public void editRow(User user) {
        selectedUser = user;
    }

    public void deleteRow(User user) {
        user.setStatus(EnumStatus.DELETED);
    }



}
