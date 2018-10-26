package ru.xenya.market.backend.data;

public class Role {
    public static final String USER = "users";
    public static final String MANAGER = "manager";

    //Эта роль дает доступ ко всем представлениям
    public static final String ADMIN = "admin";

    private Role(){
        //только статичные методы и поля
    }

    public static String[] getRoles(){
        return new String[]{USER, MANAGER, ADMIN};
    }
}
