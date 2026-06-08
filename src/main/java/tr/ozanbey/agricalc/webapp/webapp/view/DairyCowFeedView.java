package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.NotNull;
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
public class DairyCowFeedView implements Serializable {

    private Long userFeedId;
    private Long selectedFeedId;
    private Feed feed;

    private Double amountKg;
    @NotNull(message = "Satın alma tarihi seçiniz")
    private LocalDateTime buyingDate;
    @NotNull(message = "Kilogram fiyatını giriniz")
    private BigDecimal buyingPriceKg;

    private Double lactationRasyon;
    private Double roughageRasyon;

    public DairyCowFeedView(Long userFeedId, Long selectedFeedId, Feed feed, Double amountKg, LocalDateTime buyingDate, BigDecimal buyingPriceKg) {
        this.userFeedId = userFeedId;
        this.selectedFeedId = selectedFeedId;
        this.feed = feed;
        this.amountKg = amountKg;
        this.buyingDate = buyingDate;
        this.buyingPriceKg = buyingPriceKg;
    }

    public DairyCowFeedView(Long userFeedId, Feed feed, BigDecimal buyingPriceKg, Double lactationRasyon, Double roughageRasyon) {
        this.userFeedId = userFeedId;
        this.feed = feed;
        this.buyingPriceKg = buyingPriceKg;
        this.lactationRasyon = lactationRasyon;
        this.roughageRasyon = roughageRasyon;
    }
}
