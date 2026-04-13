package tr.ozanbey.agricalc.webapp.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.converter.EnumStatusConverter;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AbstractStatusEntity extends AbstractEntity {

    @Column(name = "udate", insertable = false)
    @LastModifiedDate
    private LocalDateTime updateDate;

    @Convert(converter = EnumStatusConverter.class)
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private EnumStatus status;

}
