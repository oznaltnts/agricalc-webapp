package tr.ozanbey.agricalc.webapp.service.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserInformation;
import tr.ozanbey.agricalc.webapp.service.domain.UserPreference;
import tr.ozanbey.agricalc.webapp.service.domain.UserRole;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumRole;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.UserInformationRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserPreferenceRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserRoleRepository;
import tr.ozanbey.agricalc.webapp.webapp.util.helpers.DateHelper;
import tr.ozanbey.agricalc.webapp.webapp.util.io.CryptoUtils;

import java.util.ArrayList;
import java.util.List;
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
        return preferenceRepository.findByUserId(userId);
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

    public UserInformation getInformationByUserId(Long userId) {
        return informationRepository.findByUserId(userId);
    }

}
