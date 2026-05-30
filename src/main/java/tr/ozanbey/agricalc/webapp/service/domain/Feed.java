package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.*;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedType;

@Getter
@Setter
@Entity
@Table(name = "feeds")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Feed extends AbstractStatusEntity {


    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumFeedCategory feedCategory;

    @Column(name = "feed_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EnumFeedType feedType;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

}
