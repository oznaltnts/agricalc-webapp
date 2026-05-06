package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserPreference;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.UserPreferenceRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserRoleRepository;

import java.util.Optional;

@Service
@Slf4j
public class UserService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    public UserPreference getPreferenceByUserId(Long userId) {
        return userPreferenceRepository.findByUserId(userId);
    }

    public Optional<User> getUserIdByPhoneAndStatus(String phone, EnumStatus status) {
        return userRepository.findByPhoneAndStatus(phone, status);
    }

}
