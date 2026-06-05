package tr.ozanbey.agricalc.webapp.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowBarn;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.util.List;
import java.util.Optional;

public interface UserDairyCowBarnRepository extends JpaRepository<UserDairyCowBarn, Long> {

    @EntityGraph(attributePaths = {"dairyCow"})
    Optional<UserDairyCowBarn> findByIdAndUser_Id(Long id, Long userId);

    @EntityGraph(attributePaths = {"dairyCow"})
    List<UserDairyCowBarn> findByStatusInAndUser_Id(EnumStatus[] status, Long userId);

}
