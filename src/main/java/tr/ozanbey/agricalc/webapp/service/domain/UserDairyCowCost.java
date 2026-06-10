package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "user_dairy_cow_costs")
@NoArgsConstructor
@AllArgsConstructor
public class UserDairyCowCost extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_dairy_cow_barn_id", referencedColumnName = "id", nullable = false)
    private UserDairyCowBarn userDairyCowBarn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_id", referencedColumnName = "id", nullable = false)
    private DairyCowCost dairyCowCost;

    @Column(name = "count")
    @ToString.Include
    private Double count;

    @Column(name = "total_cost", nullable = false)
    @ToString.Include
    private BigDecimal totalCost;

    @Column(name = "hourly_interest")
    @ToString.Include
    private Double hourlyOrInterest;

    @Column(name = "cost_name")
    @ToString.Include
    private String costName;


}
