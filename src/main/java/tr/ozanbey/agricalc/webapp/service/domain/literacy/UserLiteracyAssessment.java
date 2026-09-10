package tr.ozanbey.agricalc.webapp.service.domain.literacy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import tr.ozanbey.agricalc.webapp.service.domain.AbstractStatusEntity;
import tr.ozanbey.agricalc.webapp.service.domain.User;

import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_literacy_assessments")
@ToString(onlyExplicitlyIncluded = true)
public class UserLiteracyAssessment extends AbstractStatusEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "total_score")
    @ToString.Include
    private Double totalScore;

    @Column(name = "budget_score")
    @ToString.Include
    private Double budgetScore;

    @Column(name = "debt_score")
    @ToString.Include
    private Double debtScore;

    @Column(name = "operational_score")
    @ToString.Include
    private Double operationalScore;

    @Column(name = "saving_score")
    @ToString.Include
    private Double savingScore;

    @OneToMany(mappedBy = "userLiteracyAssessment", fetch = FetchType.LAZY)
    private List<UserLiteracyAnswer> literacyAnswerList;

}
