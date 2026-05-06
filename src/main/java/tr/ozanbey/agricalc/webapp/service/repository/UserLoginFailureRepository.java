package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserLoginFailure;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.time.LocalDateTime;

public interface UserLoginFailureRepository extends JpaRepository<UserLoginFailure, Long> {

    long countByUserIdAndStatusAndInsertDateGreaterThan(Long userId, EnumStatus status, LocalDateTime threshold);

    @Modifying
    @Query("UPDATE UserLoginFailure lf SET lf.status = :status WHERE lf.user.id = :userId AND lf.status = 1")
    void updateStatusByUserId(@Param("userId") Long userId, @Param("status") int status);

}
