package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumSeedAndSeedlingNumberType;

import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "city_crop_seed_and_seedling_numbers")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CityCropSeedAndSeedlingNumber extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_crop_id", referencedColumnName = "id", nullable = false)
    private CityCrop cityCrop;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumSeedAndSeedlingNumberType type;

    @OneToMany(mappedBy = "seedSeedlingNumber", fetch = FetchType.LAZY)
    private List<CityCropSeedAndSeedlingNumberValue> cityCropSeedAndSeedlingNumberValueList;

}
