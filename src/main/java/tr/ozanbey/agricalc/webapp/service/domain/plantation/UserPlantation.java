package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.domain.CityDistrict;
import tr.ozanbey.agricalc.webapp.service.domain.User;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_plantations")
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantation extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Parsel Bilgisi
    @Column(name = "name", length = 118, nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "area", nullable = false)
    @ToString.Include
    private Double area;

    @Column(name = "owned_or_rental", columnDefinition = "TINYINT")
    private Boolean ownedOrRental;

    @Column(name = "sell_or_rent_price", precision = 15, scale = 3)
    @ToString.Include
    private BigDecimal sellOrRentPrice;

    // Adres Bilgisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_district_id", referencedColumnName = "id", nullable = false)
    private CityDistrict district;

    @Column(name = "village", length = 118)
    @ToString.Include
    private String village;

    @Column(name = "neighborhood", length = 118)
    @ToString.Include
    private String neighborhood;

    @Column(name = "ada_number")
    @ToString.Include
    private Integer adaNumber;

    @Column(name = "pafta_number")
    @ToString.Include
    private Integer paftaNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orchard_product_id", referencedColumnName = "id")
    private PlantationProduct orchardProduct;
    // Arazi Yapısı
    @Column(name = "status_type", length = 33)
    @ToString.Include
    private String statusType;

    @Column(name = "nadas", columnDefinition = "TINYINT")
    private Boolean nadas;

    @Column(name = "slope", length = 33)
    @ToString.Include
    private String slope;

    @Column(name = "orientation", length = 33)
    @ToString.Include
    private String orientation;

    // Toprak Özellikleri
    @Column(name = "soil_texture", length = 33)
    @ToString.Include
    private String soilTexture;

    @Column(name = "soil_depth", length = 33)
    @ToString.Include
    private String soilDepth;

    @Column(name = "organic_matter", length = 33)
    @ToString.Include
    private String organicMatter;

    @Column(name = "soil_salinity", length = 33)
    @ToString.Include
    private String soilSalinity;

    @Column(name = "lime", length = 33)
    @ToString.Include
    private String lime;

    @Column(name = "phosphorus", length = 33)
    @ToString.Include
    private String phosphorus;

    @Column(name = "potassium", length = 33)
    @ToString.Include
    private String potassium;

    @OneToMany(mappedBy = "userPlantation", fetch = FetchType.LAZY)
    private List<UserPlantationAnswer> answerList;

    @OneToMany(mappedBy = "userPlantation", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @SQLRestriction("status IN (0, 1)")
    private List<UserPlantationPlan> plantationPlanList;

}
