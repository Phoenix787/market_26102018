package ru.xenya.market.ui.views;

import ru.xenya.market.ui.components.common.ConfirmationDialog;

public interface HasConfirmation<E> {
    void setConfirmDialog(ConfirmationDialog<E> confirmDialog);

    ConfirmationDialog getConfirmDialog();
}
