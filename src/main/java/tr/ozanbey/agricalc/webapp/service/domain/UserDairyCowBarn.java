package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_dairy_cow_barns")
@NoArgsConstructor
@AllArgsConstructor
public class UserDairyCowBarn extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dairy_cow_id", referencedColumnName = "id", nullable = false)
    private DairyCow dairyCow;

    @Column(name = "barn_capacity", nullable = false)
    @ToString.Include
    private int barnCapacity;

    @Column(name = "milking_capacity", nullable = false)
    @ToString.Include
    private int milkingCapacity;

    @Column(name = "barn_price")
    @ToString.Include
    private BigDecimal barnPrice;

    @Column(name = "birth_rate")
    @ToString.Include
    private Double birthRate;

    @Column(name = "death_rate")
    @ToString.Include
    private Double deathRate;

    @Column(name = "insemination_rate")
    @ToString.Include
    private Double inseminationRate;

    @Column(name = "milk_yield")
    @ToString.Include
    private Double milkYield;

    @Column(name = "lactation_period", nullable = false)
    @ToString.Include
    private Integer lactationPeriod;

    @Column(name = "total_count")
    @ToString.Include
    private Double totalCount;

    @Column(name = "end_year_total_count")
    @ToString.Include
    private Double endYearTotalCount;

    @Column(name = "average_feed_total_count")
    @ToString.Include
    private Double averageFeedTotalCount;

    @Column(name = "average_milking_count")
    @ToString.Include
    private Double averageMilkingCount;

    @OneToMany(mappedBy = "userDairyCowBarn", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDairyCowCount> dairyCowCountList;

    @OneToMany(mappedBy = "userDairyCowBarn", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDairyCowFeed> dairyCowFeedList;

    @OneToMany(mappedBy = "userDairyCowBarn", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDairyCowCost> dairyCowCostList;

}
