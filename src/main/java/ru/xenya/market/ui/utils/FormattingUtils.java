package ru.xenya.market.ui.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class FormattingUtils {
    public static final String DECIMAL_ZERO = "0.00";

    public static final DateTimeFormatter WEEKDAY_FULLNAME_FORMATTER = DateTimeFormatter.ofPattern("EEEE", MarketConst.APP_LOCALE);

    public static final DateTimeFormatter FULL_DAY_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy", MarketConst.APP_LOCALE);

    public static String getFullMonthName(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL, MarketConst.APP_LOCALE);
    }

    public static String formatAsCurrency (int valueInCop){
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(MarketConst.APP_LOCALE);
        numberFormat.setCurrency(Currency.getInstance(new Locale("ru", "RU")));
        return numberFormat.format(BigDecimal.valueOf(valueInCop, 2));

    }

    public static String formatAsDouble(double value){
        return NumberFormat.getCurrencyInstance(MarketConst.APP_LOCALE).format(Double.valueOf(value));
    }

    public static String formatAsDouble(int value){
        DecimalFormat uiAmountFormatter = getUiAmountFormatter();
        return uiAmountFormatter.format(BigDecimal.valueOf(value, 2 ));
    }

    //₽
    public static DecimalFormat getUiPriceFormatter(){
        DecimalFormat formatter = new DecimalFormat("#" + DECIMAL_ZERO,
                DecimalFormatSymbols.getInstance(MarketConst.APP_LOCALE));
        formatter.setGroupingUsed(false);
        return formatter;
    }

    public static DecimalFormat getUiAmountFormatter(){
        DecimalFormat formatter = new DecimalFormat("#" + DECIMAL_ZERO);
        formatter.setGroupingUsed(false);
        return formatter;
    }

    public static String formatListSize(List list) {
        int size = list.size();
        return String.valueOf(size);
    }
}
