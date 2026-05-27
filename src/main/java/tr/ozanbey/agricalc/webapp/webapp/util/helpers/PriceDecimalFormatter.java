package tr.ozanbey.agricalc.webapp.webapp.util.helpers;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Component("priceDecimalFormatter")
public class PriceDecimalFormatter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o != null && !o.toString().trim().isEmpty()) {

            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
            formatter.setRoundingMode(RoundingMode.HALF_UP);
            try {
                return addTrailingZeroes(formatter.format(new BigDecimal(o.toString())));
            } catch (Exception e) {
                return o.toString();
            }
        } else {
            return null;
        }
    }

    private String addTrailingZeroes(String value) {
        String[] parts = value.split(",");
        if (parts.length == 1)
            return value + ",00";
        else {
            String partAfterDot = parts[1];
            if (partAfterDot.length() == 1)
                return value + "0";
            else return value;
        }
    }

}
