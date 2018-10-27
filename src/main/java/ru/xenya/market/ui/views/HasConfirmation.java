package ru.xenya.market.ui.views;

import ru.xenya.market.ui.components.common.ConfirmDialog;
import ru.xenya.market.ui.components.common.ConfirmationDialog;

public interface HasConfirmation<E> {
    void setConfirmDialog(ConfirmDialog confirmDialog);

    ConfirmDialog getConfirmDialog();
}
