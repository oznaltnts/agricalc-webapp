package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.domain.Feed;
import tr.ozanbey.agricalc.webapp.service.domain.UserDairyCowFeed;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DairyCowFeedView implements Serializable {

    private UserDairyCowFeed userFeed;

    @NotNull(message = "Yaklaşık miktarı kilogram olarak giriniz")
    @Positive
    private BigDecimal amountKg;
    @NotNull(message = "Satın alma tarihi seçiniz")
    @PastOrPresent
    private LocalDateTime buyingDate;
    @NotNull(message = "Kilogram fiyatını giriniz")
    @Positive
    private BigDecimal buyingPriceKg;

    public DairyCowFeedView(UserDairyCowFeed userFeed, Feed feed, BigDecimal amountKg, LocalDateTime buyingDate, BigDecimal buyingPriceKg) {
        this.userFeed = Objects.requireNonNullElseGet(userFeed, () -> new UserDairyCowFeed(feed));
        this.amountKg = amountKg;
        this.buyingDate = buyingDate;
        this.buyingPriceKg = buyingPriceKg;
    }
}
