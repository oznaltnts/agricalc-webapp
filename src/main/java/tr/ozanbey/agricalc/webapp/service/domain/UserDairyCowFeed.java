package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_dairy_cow_feeds")
@NoArgsConstructor
@AllArgsConstructor
public class UserDairyCowFeed extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_dairy_cow_barn_id", referencedColumnName = "id", nullable = false)
    private UserDairyCowBarn userDairyCowBarn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", referencedColumnName = "id", nullable = false)
    private Feed feed;

    @Column(name = "amount_kg", nullable = false)
    @ToString.Include
    private double amountKg;

    @Column(name = "buying_date", nullable = false)
    @ToString.Include
    private LocalDateTime buyingDate;

    @Column(name = "buying_price", nullable = false)
    @ToString.Include
    private BigDecimal buyingPrice;

}
