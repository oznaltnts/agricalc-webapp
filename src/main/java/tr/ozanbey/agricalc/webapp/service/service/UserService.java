package tr.ozanbey.agricalc.webapp.service.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumRole;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.UserInformationRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserPreferenceRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserRoleRepository;
import tr.ozanbey.agricalc.webapp.webapp.util.helpers.DateHelper;
import tr.ozanbey.agricalc.webapp.webapp.util.io.CryptoUtils;
import tr.ozanbey.agricalc.webapp.webapp.view.UserInformationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    @Autowired
    private UserPreferenceRepository preferenceRepository;

    @Autowired
    private UserInformationRepository informationRepository;

    public UserPreference getPreferenceByUserId(Long userId) {
        return preferenceRepository.findByUser_Id(userId);
    }

    public Optional<User> getUserIdByPhoneAndStatus(String phone, EnumStatus status) {
        return userRepository.findByPhoneAndStatus(phone, status);
    }

    @Transactional
    public void registerUser(String phone, String password) throws Exception {
        String formatted = "+90" + phone.replaceAll("\\D", "");
        if (checkUserExistByPhone(formatted)) {
            throw new Exception("Bu numara ile kullanıcı mevcut");
        }

        User user = saveUser(formatted, password);
        generatePreferenceForUser(user);
    }

    private User saveUser(String formatted, String password) {
        User user = new User();
        user.setStatus(EnumStatus.ACTIVE);
        user.setPhone(formatted);
        user.setPassword(CryptoUtils.oneWayHash(password));
        assignRoleToUser(user, EnumRole.USER);
        return userRepository.save(user);
    }

    private void assignRoleToUser(User user, EnumRole enumRole) {
        List<UserRole> userRoleList = new ArrayList<>();
        UserRole role = new UserRole(user, enumRole);
        userRoleList.add(role);
        user.setRoleList(userRoleList);
    }

    private boolean checkUserExistByPhone(String phone) {
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        return optionalUser.isPresent();
    }

    private void generatePreferenceForUser(User user) {
        UserPreference userPreference = UserPreference.createWithTemplateData(user);
        preferenceRepository.save(userPreference);
    }

    @Transactional
    public void updateLastLoginInfo(User user) {
        user.setBeforeLastLogin(user.getLastLogin());
        user.setLastLogin(DateHelper.now());
        userRepository.save(user);
    }

    public UserInformationView getInformationByUserId() {
        UserInformation information = informationRepository.findByUser_Id(getCurrentUser().getUser().getId());
        UserInformationView view = new UserInformationView();
        if (information != null) {
            if (information.getCity() != null)
                view.setUserCityId(information.getCity().getId());
            view.setTckn(information.getTckn());
            view.setName(information.getNameSurname());
            view.setEmail(information.getEmail());
            view.setDistrict(information.getDistrict());
            view.setVillage(information.getVillage());
            view.setNeighborhood(information.getNeighborhood());
        }
        return view;
    }

    @Transactional
    public boolean save(UserInformationView informationView) {
        UserInformation information = informationRepository.findByUser_Id(getCurrentUser().getUser().getId());
        if (checkIsThereDifference(informationView, information)) {
            if (information == null) {
                information = new UserInformation();
            }
            viewToEntity(informationView, information);
            informationRepository.save(information);
            return true;
        }
        return false;
    }

    private void viewToEntity(UserInformationView informationView, UserInformation information) {
        information.setUser(getCurrentUser().getUser());
        if (informationView.getUserCityId() != null) {
            information.setCity(new City(informationView.getUserCityId()));
        }
        information.setTckn(informationView.getTckn());
        information.setNameSurname(informationView.getName());
        information.setEmail(informationView.getEmail());
        information.setDistrict(informationView.getDistrict());
        information.setVillage(informationView.getVillage());
        information.setNeighborhood(informationView.getNeighborhood());
    }

    private boolean checkIsThereDifference(UserInformationView view, UserInformation info) {
        if (info == null && view == null)
            return false;

        if (info == null || view == null)
            return true;

        return !Objects.equals(info.getCity() != null ? info.getCity().getId() : null, view.getUserCityId())
                || !Objects.equals(info.getTckn(), view.getTckn())
                || !Objects.equals(info.getNameSurname(), view.getName())
                || !Objects.equals(info.getEmail(), view.getEmail())
                || !Objects.equals(info.getDistrict(), view.getDistrict())
                || !Objects.equals(info.getVillage(), view.getVillage())
                || !Objects.equals(info.getNeighborhood(), view.getNeighborhood());
    }

    public void saveUserPreferences(String menuMode,
                                    String darkMode,
                                    String componentTheme,
                                    String topbarTheme,
                                    String menuTheme,
                                    String inputStyle,
                                    boolean lightLogo) {
        UserPreference userPreference = preferenceRepository.findByUser_Id(getCurrentUser().getUser().getId());
        userPreference.setMenuMode(menuMode);
        userPreference.setDarkMode(darkMode);
        userPreference.setComponentTheme(componentTheme);
        userPreference.setTopbarTheme(topbarTheme);
        userPreference.setMenuTheme(menuTheme);
        userPreference.setInputStyle(inputStyle);
        userPreference.setLightLogo(lightLogo);
        preferenceRepository.save(userPreference);
    }


}
