package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "user_dairy_cow_counts")
@NoArgsConstructor
@AllArgsConstructor
public class UserDairyCowCount extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_dairy_cow_barn_id", referencedColumnName = "id", nullable = false)
    private UserDairyCowBarn userDairyCowBarn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dairy_cow_coefficient_id", referencedColumnName = "id", nullable = false)
    private DairyCowCoefficient dairyCowCoefficient;

    @Column(name = "current_count")
    @ToString.Include
    private Integer currentCount;

    @Column(name = "purchase_count")
    @ToString.Include
    private Integer purchaseCount;

    @Column(name = "sell_count")
    @ToString.Include
    private Integer sellCount;

    @Column(name = "end_year_count")
    @ToString.Include
    private Double endYearCount;

    @Column(name = "average_feed_count")
    @ToString.Include
    private Double averageFeedCount;

}
