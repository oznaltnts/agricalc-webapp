package tr.ozanbey.agricalc.webapp.service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.domain.UserLoginFailure;
import tr.ozanbey.agricalc.webapp.service.domain.UserLoginSuccess;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.repository.UserLoginFailureRepository;
import tr.ozanbey.agricalc.webapp.service.repository.UserLoginSuccessRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserLoginService extends BaseService {

    @Autowired
    private UserLoginFailureRepository userLoginFailureRepository;

    @Autowired
    private UserLoginSuccessRepository userLoginSuccessRepository;

    public long isAccountBlocked(Long userId) {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        return userLoginFailureRepository.countByUserIdAndStatusAndInsertDateGreaterThan(userId, EnumStatus.ACTIVE, threshold);
    }

    @Transactional
    public void clearLoginFailures(Long userId) {
        userLoginFailureRepository.updateStatusByUserId(userId, EnumStatus.DELETED.getValue());
    }

    @Transactional
    public void createLoginSuccess(Long userId) {
        UserLoginSuccess userLoginSuccess = new UserLoginSuccess();
        userLoginSuccess.setIpAddress(getIpAddress());
        userLoginSuccess.setUser(new User(userId));
        userLoginSuccessRepository.save(userLoginSuccess);
    }

    @Transactional
    public void createLoginFailure(Long userId) {
        UserLoginFailure userLoginFailure = new UserLoginFailure();
        userLoginFailure.setUser(new User(userId));
        userLoginFailure.setIpAddress(getIpAddress());
        userLoginFailureRepository.save(userLoginFailure);

    }

}
