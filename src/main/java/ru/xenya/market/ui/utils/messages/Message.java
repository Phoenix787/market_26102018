package ru.xenya.market.ui.utils.messages;

public class Message {
    public static final String CONFIRM_CAPTION_DELETE = "Подтвердить удаление";
    public static final String CONFIRM_MESSAGE_DELETE = "Вы уверены вы хотите удалить выбранный элемент? Эта операция не может быть отменена.";
    public static final String BUTTON_CAPTION_DELETE = "Удалить";
    public static final String BUTTON_CAPTION_CANCEL = "Отменить";

    public static final MessageSupplier UNSAVED_CHANGES = createMessage("Несохраненные данные",
            "Отклонить", "Продолжить редактирование",
            "Несохраненные изменения в %s. Отменить изменения?");

    public static final MessageSupplier CONFIRM_DELETE = createMessage(CONFIRM_CAPTION_DELETE, BUTTON_CAPTION_DELETE,
            BUTTON_CAPTION_CANCEL, CONFIRM_MESSAGE_DELETE);

    private final String caption;
    private final String okText;
    private final String cancelText;
    private final String message;

    public Message(String caption, String okText, String cancelText, String message) {
        this.caption = caption;
        this.okText = okText;
        this.cancelText = cancelText;
        this.message = message;
    }

    private static MessageSupplier createMessage(String caption, String okText, String cancelText, String message) {
        return (parameters) -> new Message(caption, okText, cancelText, String.format(message, parameters));
    }

    public String getCaption() {
        return caption;
    }

    public String getOkText() {
        return okText;
    }

    public String getCancelText() {
        return cancelText;
    }

    public String getMessage() {
        return message;
    }

    @FunctionalInterface
    public interface MessageSupplier {
        Message createMessage(Object... parameters);
    }
}
