package ru.xenya.market.ui.views.admin.prices.utils;

import org.junit.Test;
import ru.xenya.market.ui.utils.MarketConst;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import static org.junit.Assert.*;

public class PriceConverterTest {

    @Test
    public void convertToModel() throws ParseException {

        DecimalFormat df = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(MarketConst.APP_LOCALE));
        df.setGroupingUsed(false);

        Integer tmp;
        Double dbl;
        String value = "23,35";
            dbl = df.parse(value).doubleValue();
           // dbl = dbl *100;
            tmp = (int) (dbl * 100);
            assertEquals(java.util.Optional.of(2335), java.util.Optional.of(tmp));

    }

    @Test
    public void convertToPresentation() {
    }
}