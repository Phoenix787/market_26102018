package ru.xenya.market.ui.utils;

import org.springframework.data.domain.Sort;

import java.util.Locale;

public class MarketConst {

    public static final Locale APP_LOCALE = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();

    public static final String PAGE_ROOT = "";
    public static final String PAGE_STOREFRONT = "storefront";
    public static final String PAGE_STOREFRONT_EDIT = "storefront/edit";
    public static final String PAGE_CUSTOMERS = "customers";
    public static final String PAGE_USERS = "users";
    public static final String PAGE_PRODUCTS = "products";
    public static final String PAGE_PRODUCTS_EDIT = "products/edit";
    public static final String PAGE_LOGOUT = "logout";
    public static final String PAGE_DEFAULT = PAGE_STOREFRONT;

    public static final String ICON_STOREFRONT = "cart";
    public static final String ICON_CUSTOMERS = "group";
    public static final String ICON_USERS = "users";
    public static final String ICON_PRODUCTS = "calendar";
    public static final String ICON_LOGOUT = "arrow-right";

    public static final String TITLE_STOREFRONT = "Заказы";
    public static final String TITLE_CUSTOMERS = "Контрагенты";
    public static final String TITLE_USERS = "Пользователи";
    public static final String TITLE_PRODUCTS = "Прайс";
    public static final String TITLE_LOGOUT = "Выход";
    public static final String TITLE_NOT_FOUND = "Страница не найдена";

    public static final String[] ORDER_SORT_FIELDS = {"dueDate", "id"};
    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    public static final String VIEWPORT = "width=device-width, minimum-scale=1, initial-scale=1, users-scalable=yes";

    public static final int NOTIFICATION_DURATION = 4000;
}
