package tr.ozanbey.agricalc.webapp.service.domain.plantation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractEntity;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.User;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelType;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_plant_parcels")
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantParcel extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private PlantationProduct product;

    // Parsel Bilgisi
    @Column(name = "parcel_type")
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumParcelType parcelType;

    @Column(name = "parcel_name")
    @ToString.Include
    private String parcelName;

    @Column(name = "parcel_price")
    @ToString.Include
    private BigDecimal parcelPrice;

    @Column(name = "area_decare", nullable = false)
    @ToString.Include
    private Double areaDecare;

    @Column(name = "rent_price")
    @ToString.Include
    private BigDecimal rentPrice;

    // Adres Bilgisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @Column(name = "district")
    @ToString.Include
    private String district;

    @Column(name = "village")
    @ToString.Include
    private String village;

    @Column(name = "neighborhood")
    @ToString.Include
    private String neighborhood;

    @Column(name = "ada_number")
    @ToString.Include
    private Integer adaNumber;

    @Column(name = "pafta_number")
    @ToString.Include
    private Integer paftaNumber;

    // Arazi Yapısı
    @Column(name = "status_type")
    @ToString.Include
    private String statusType;

    @Column(name = "nadas", columnDefinition = "TINYINT")
    private Boolean nadas;

    @Column(name = "slope")
    @ToString.Include
    private String slope;

    @Column(name = "orientation")
    @ToString.Include
    private String orientation;

    // Toprak Özellikleri
    @Column(name = "soil_texture")
    @ToString.Include
    private String soilTexture;

    @Column(name = "soil_depth")
    @ToString.Include
    private String soilDepth;

    @Column(name = "organic_matter")
    @ToString.Include
    private String organicMatter;

    @Column(name = "soil_salinity")
    @ToString.Include
    private String soilSalinity;

    @Column(name = "lime")
    @ToString.Include
    private String lime;

    @Column(name = "phosphorus")
    @ToString.Include
    private String phosphorus;

    @Column(name = "potassium")
    @ToString.Include
    private String potassium;

    // Sulama Bilgileri
    @Column(name = "watering_source")
    @ToString.Include
    private String wateringSource;

    @Column(name = "watering_type")
    @ToString.Include
    private String wateringType;

    @Column(name = "electric_source")
    @ToString.Include
    private String electricSource;

    @OneToMany(mappedBy = "plantParcel", fetch = FetchType.LAZY)
    private List<UserPlantParcelAnswer> parcelAnswerList;

}
