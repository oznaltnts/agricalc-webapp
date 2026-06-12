package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "user_dairy_cow_incomes")
@NoArgsConstructor
@AllArgsConstructor
public class UserDairyCowIncome extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_dairy_cow_barn_id", referencedColumnName = "id", nullable = false)
    private UserDairyCowBarn userDairyCowBarn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dairy_cow_income_id", referencedColumnName = "id", nullable = false)
    private DairyCowIncome dairyCowIncome;

    @Column(name = "income_value", nullable = false)
    @ToString.Include
    private BigDecimal incomeValue;

}
