package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserRole;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumRole;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.UserRepository;
import tr.ozanbey.agricalc.webapp.webapp.util.helpers.DateHelper;
import tr.ozanbey.agricalc.webapp.webapp.util.io.CryptoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserIdByPhoneAndStatus(String phone, EnumStatus status) {
        return userRepository.findByPhoneAndStatus(phone, status);
    }

    @Transactional
    public void registerUser(String phone, String password) throws Exception {
        String formatted = "+90" + phone.replaceAll("\\D", "");
        if (checkUserExistByPhone(formatted)) {
            throw new Exception("Bu numara ile kullanıcı mevcut");
        }

        saveUser(formatted, password);
    }

    private void saveUser(String formatted, String password) {
        User user = new User();
        user.setStatus(EnumStatus.ACTIVE);
        user.setPhone(formatted);
        user.setPassword(CryptoUtils.oneWayHash(password));
        assignRoleToUser(user);
        userRepository.save(user);
    }

    private void assignRoleToUser(User user) {
        List<UserRole> userRoleList = new ArrayList<>();
        UserRole role = new UserRole(user, EnumRole.USER);
        userRoleList.add(role);
        user.setRoleList(userRoleList);
    }

    private boolean checkUserExistByPhone(String phone) {
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        return optionalUser.isPresent();
    }

    @Transactional
    public void updateLastLoginInfo(User user) {
        user.setBeforeLastLogin(user.getLastLogin());
        user.setLastLogin(DateHelper.now());
        userRepository.save(user);
    }

}
