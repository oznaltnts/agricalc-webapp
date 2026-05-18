package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantAsset;

import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_plant_assets")
@ToString(onlyExplicitlyIncluded = true)
public class UserPlantAsset extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "plant_asset", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumPlantAsset plantAsset;

    @Column(name = "quantity", nullable = false)
    @ToString.Include
    private Integer quantity;

    @OneToMany(mappedBy = "userPlantAsset", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPlantAssetDetail> plantAssetDetailList;

}
