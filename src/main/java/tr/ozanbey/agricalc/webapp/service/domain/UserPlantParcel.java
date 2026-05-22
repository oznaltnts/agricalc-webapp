package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelType;

import java.math.BigDecimal;

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

    @Column(name = "parcel_type")
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumParcelType parcelType;

    // Parsel Bilgisi
    @Column(name = "parce_name")
    @ToString.Include
    private String parcelName;

    @Column(name = "parcel_price")
    @ToString.Include
    private BigDecimal parcelPrice;

    @Column(name = "ada_number")
    @ToString.Include
    private Integer adaNumber;

    @Column(name = "pafta_number")
    @ToString.Include
    private Integer paftaNumber;

    @Column(name = "area_decare")
    @ToString.Include
    private BigDecimal areaDecare;

    @Column(name = "rent_price")
    @ToString.Include
    private BigDecimal rentPrice;

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

}
