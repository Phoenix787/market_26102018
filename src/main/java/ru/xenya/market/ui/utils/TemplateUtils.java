package ru.xenya.market.ui.utils;

public class TemplateUtils {
    public static String generateLocation(String basePage, String entityId) {
        return basePage + (entityId == null || entityId.isEmpty() ? "" : "/" + entityId);
    }
}
