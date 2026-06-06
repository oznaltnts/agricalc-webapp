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

    private double amountKg;
    @NotNull(message = "Satın alma tarihi seçiniz")
    private LocalDateTime buyingDate;
    @NotNull(message = "Kilogram fiyatını giriniz")
    private BigDecimal buyingPriceKg;

}
