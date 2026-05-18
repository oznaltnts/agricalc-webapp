package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_plant_asset_details")
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantAssetDetail extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_plant_asset_id", referencedColumnName = "id", nullable = false)
    private UserPlantAsset userPlantAsset;

    @Column(name = "asset_model")
    @ToString.Include
    private String assetModel;

    @Column(name = "asset_price")
    @ToString.Include
    private BigDecimal assetPrice;

    @Column(name = "tractor_brand")
    @ToString.Include
    private String tractorBrand;

    @Column(name = "asset_type")
    @ToString.Include
    private String assetType;

    @Column(name = "spraying_tank")
    @ToString.Include
    private BigDecimal sprayingTank;

    @Column(name = "rent_income_price")
    @ToString.Include
    private BigDecimal rentIncomePrice;

}
