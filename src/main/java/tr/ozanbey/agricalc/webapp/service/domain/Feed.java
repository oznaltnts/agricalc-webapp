package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import tr.ozanbey.agricalc.webapp.service.converter.EnumFeedCategoryConverter;
import tr.ozanbey.agricalc.webapp.service.converter.EnumFeedTypeConverter;
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
