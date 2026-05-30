package tr.ozanbey.agricalc.webapp.webapp.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DairyCowView implements Serializable {

    private Feed feed;

    private BigDecimal amountKg;
    private LocalDateTime buyingDate;
    private BigDecimal buyingPriceKg;

    public DairyCowView(Feed feed) {
        this.feed = feed;
    }

}
