package ru.xenya.market.ui.utils;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.util.Arrays;

public class TemplateUtils {
    public static String generateLocation(String basePage, String entityId) {
        return basePage + (entityId == null || entityId.isEmpty() ? "" : "/" + entityId);
    }

    public static DatePicker.DatePickerI18n setupDatePicker() {
        return new DatePicker.DatePickerI18n()
                .setWeek("неделя")
                .setCalendar("календарь")
                .setToday("сегодня")
                .setCancel("отмена")
                .setFirstDayOfWeek(1)
                .setMonthNames(Arrays.asList("январь", "февраль", "март",
                        "апрель", "май", "июнь",
                        "июль", "август", "сентябрь",
                        "октябрь", "ноябрь", "декабрь"))
                .setWeekdays(
                        Arrays.asList("воскресенье", "понедельник", "вторник",
                                "среда", "четверг", "пятница", "суббота"))
                .setWeekdaysShort(Arrays.asList("вс", "пн", "вт", "ср", "чт", "пт", "сб"));
    }
}
