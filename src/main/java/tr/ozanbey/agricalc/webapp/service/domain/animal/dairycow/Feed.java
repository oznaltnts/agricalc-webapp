package tr.ozanbey.agricalc.webapp.service.domain.animal.dairycow;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.animal.dairycow.EnumFeedCategoryConverter;
import tr.ozanbey.agricalc.webapp.service.converter.animal.dairycow.EnumFeedTypeConverter;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumFeedCategory;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumFeedType;

@Getter
@Setter
@Entity
@Table(name = "feeds")
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Feed extends AbstractStatusEntity {

    @Convert(converter = EnumFeedCategoryConverter.class)
    @Column(name = "category", nullable = false, columnDefinition = "TINYINT")
    @ToString.Include
    private EnumFeedCategory feedCategory;

    @Convert(converter = EnumFeedTypeConverter.class)
    @Column(name = "feed_type", nullable = false, columnDefinition = "TINYINT")
    @ToString.Include
    private EnumFeedType feedType;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

}
