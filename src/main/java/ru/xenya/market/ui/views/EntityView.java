package ru.xenya.market.ui.views;


import com.vaadin.flow.data.binder.ValidationException;

public interface EntityView<T> extends HasConfirmation<T>, HasNotifications {

    /**
     * Показывает уведомление об ошибке  с заданным текстом
     * @param message - дружественное для пользователя сообщение об ошибке
     * @param isPersistent если true уведомление позволяет пользователю закрывать сообщение,
     *                     если false - само автоматически закрывается через некоторое время
     */
    default void showError(String message, boolean isPersistent){
        showNotification(message, isPersistent);
    }

    /**
     * Возвращает текущее состояние диалога
     * @return true - если диалог откры в режиме Edit и имеются несохранненные изменения
     */
    boolean isDirty();

    /**
     * Удаляет ссылку на сущность и сбрасывает dirty статус
     */
    void clear();

    /**
     * Записывает изменения из диалога сущности в заданный экземпляр сущности
     * @param entity экземпляр сущности в которую записываются изменения
     * @throws ValidationException if the values entered into the entity dialog cannot be
     * 	 *             converted into entity properties
     */
    void write(T entity) throws ValidationException;

    String getEntityName();

    default void showCreatedNotification(){
        showNotification(getEntityName() + " создан");
    }

    default void showUpdateNotification() {
        showNotification(getEntityName() + " обновлен");
    }

    default void showDeleteNotification() {
        showNotification(getEntityName() + " удален");
    }

}
