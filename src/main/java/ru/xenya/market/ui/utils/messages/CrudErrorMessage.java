package ru.xenya.market.ui.utils.messages;

public class CrudErrorMessage {
    public static final String ENTITY_NOT_FOUND = "Выбранный элемент не найден";
    public static final String CONCURRENT_UPDATE = "Кто-то другой мог обновить данные. Пожалуйста, повторите попытку.";
    public static final String OPERATION_PREVENTED_BY_REFERENCES = "Операция не может быть выполнена, так как в базе данных есть ссылки на сущность.";
    public static final String REQUIRED_FIELDS_MISSING = "Пожалуйста, заполните все требуемые поля, прежде чем продолжить.";


    private CrudErrorMessage(){

    }
}
