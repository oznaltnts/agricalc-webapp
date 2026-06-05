package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "userDairyCowBarn", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDairyCowFeed> dairyCowFeedList;

    @OneToMany(mappedBy = "userDairyCowBarn", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDairyCowCount> dairyCowCountList;

}
