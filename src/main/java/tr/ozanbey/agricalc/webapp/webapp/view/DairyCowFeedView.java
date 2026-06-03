package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    private Feed feed;

    @NotNull(message = "Yaklaşık miktarı kilogram olarak giriniz")
    @Positive
    private BigDecimal amountKg;
    @NotNull(message = "Satın alma tarihi seçiniz")
    private LocalDateTime buyingDate;
    @NotNull(message = "Kilogram fiyatını giriniz")
    @Positive
    private BigDecimal buyingPriceKg;

}
