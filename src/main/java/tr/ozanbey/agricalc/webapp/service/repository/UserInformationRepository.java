package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.ozanbey.agricalc.webapp.service.domain.UserInformation;
import tr.ozanbey.agricalc.webapp.webapp.view.UserInformationView;

import java.util.List;


public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {

    UserInformation findByUser_Id(Long userId);

    @Query("""
                SELECT new tr.ozanbey.agricalc.webapp.webapp.view.UserInformationView(
                t2.id,
                t2.status,
                t2.phone,
                t4.role,
                t2.lastLogin,
                t1.tckn,
                t1.nameSurname,
                t1.email,
                t3.id,
                t3.name,
                t1.district,
                t1.village,
                t1.neighborhood)
                FROM User t2
                LEFT JOIN UserInformation t1 ON t2.id = t1.user.id
                LEFT JOIN City t3 ON t3.id = t1.city.id
                LEFT JOIN UserRole t4 on t2.id = t4.user.id
            """)
    List<UserInformationView> findAllAsInfoView();



}
