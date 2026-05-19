package tr.ozanbey.agricalc.webapp.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tr.ozanbey.agricalc.webapp.service.domain.UserPreference;


public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    UserPreference findByUser_Id(Long userId);

    @Modifying
    @Query("""
            UPDATE UserPreference up
                        SET up.menuMode = :menuMode,
                            up.darkMode = :darkMode,
                            up.componentTheme = :componentTheme,
                            up.topbarTheme = :topbarTheme,
                            up.menuTheme = :menuTheme,
                            up.inputStyle = :inputStyle,
                            up.lightLogo = :lightLogo
                        WHERE up.user.id = :userId AND up.user.status = 1
            """)
    void updatePreferenceForUser(@Param("userId") Long userId,
                                 @Param("menuMode") String menuMode,
                                 @Param("darkMode") String darkMode,
                                 @Param("componentTheme") String componentTheme,
                                 @Param("topbarTheme") String topbarTheme,
                                 @Param("menuTheme") String menuTheme,
                                 @Param("inputStyle") String inputStyle,
                                 @Param("lightLogo") boolean lightLogo
    );

}
