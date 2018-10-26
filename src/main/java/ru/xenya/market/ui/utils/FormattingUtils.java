package ru.xenya.market.ui.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

public class FormattingUtils {
    public static final String DECIMAL_ZERO = "0.00";

    public static final DateTimeFormatter WEEKDAY_FUULNAME_FORMATTER = DateTimeFormatter.ofPattern("EEEE", MarketConst.APP_LOCALE);

    public static final DateTimeFormatter FULL_DAY_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy", MarketConst.APP_LOCALE);

    public static String getFullMonthName(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL, MarketConst.APP_LOCALE);
    }

    public static String formatAsCurrency (int valueInCop){
        return NumberFormat.getCurrencyInstance(MarketConst.APP_LOCALE).format(BigDecimal.valueOf(valueInCop, 2));
    }

    public static DecimalFormat getUiPriceFormatter(){
        DecimalFormat formatter = new DecimalFormat("#" + DECIMAL_ZERO,
                DecimalFormatSymbols.getInstance(MarketConst.APP_LOCALE));
        formatter.setGroupingUsed(false);
        return formatter;
    }
}
