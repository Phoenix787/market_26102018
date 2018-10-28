package ru.xenya.market.ui.crud;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import ru.xenya.market.app.HasLogger;
import ru.xenya.market.backend.data.entity.AbstractEntity;
import ru.xenya.market.backend.data.entity.User;
import ru.xenya.market.backend.data.entity.util.EntityUtil;
import ru.xenya.market.backend.service.CrudService;
import ru.xenya.market.backend.service.UserFriendlyDataException;
import ru.xenya.market.ui.utils.messages.Message;
import ru.xenya.market.ui.utils.messages.CrudErrorMessage;
import ru.xenya.market.ui.views.EntityView;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.function.UnaryOperator;

public class EntityPresenter<T extends AbstractEntity, V extends EntityView<T>>
        implements HasLogger {
    private CrudService<T> crudService;
    private User currentUser;
    private V view;

    private EntityPresenterState<T> state = new EntityPresenterState<>();

    public EntityPresenter(CrudService<T> crudService, User currentUser) {
        this.crudService = crudService;
        this.currentUser = currentUser;
    }

    public void setView(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }

    public void delete(CrudOperationListener<T> onSuccess) {
        Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
        confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, ()->{
            if (executeOperation(() -> crudService.delete(currentUser, state.getEntity()))) {
                onSuccess.execute(state.getEntity());
            }
        }, ()->{

        });
    }

    public void save(CrudOperationListener<T> onSuccess) {
        if (executeOperation(()->saveEntity())){
            onSuccess.execute(state.getEntity());
        }
    }

    public boolean executeUpdate(UnaryOperator<T> updater) {
        return executeOperation(() -> state.updateEntity(updater.apply(getEntity()), isNew()));
    }

    private boolean executeOperation(Runnable operation) {
        try {
            operation.run();
            return true;
        } catch (UserFriendlyDataException e) {
            consumeError(e, e.getMessage(), true);
        } catch (DataIntegrityViolationException e) {
            consumeError(e, CrudErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
        } catch (OptimisticLockingFailureException e) {
            consumeError(e, CrudErrorMessage.CONCURRENT_UPDATE, true);
        } catch (EntityNotFoundException e) {
            consumeError(e, CrudErrorMessage.ENTITY_NOT_FOUND, false);
        } catch (ConstraintViolationException e) {
            consumeError(e, CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
        }
        return false;
    }

    private void consumeError(Exception e, String message, boolean isPersistent) {
        getLogger().debug(message, e);
        view.showError(message, isPersistent);
    }

    private void saveEntity(){
        state.updateEntity(crudService.save(currentUser, state.getEntity()), isNew());
    }

    public boolean writeEntity() {
        try {
            System.err.println(state.getEntityName());
            view.write(state.getEntity());
            return true;
        } catch (ValidationException e) {
            view.showError(CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
            return false;
        } catch (NullPointerException e) {
            Notification.show("NullPointerException");
            return false;
        }
    }

    public void close() {
        state.clear();
        view.clear();
    }


    public void cancel(Runnable onConfirmed, Runnable onCancelled) {
        confirmIfNecessaryAndExecute(view.isDirty(), Message.UNSAVED_CHANGES.createMessage(state.getEntityName()), () -> {
            view.clear();
            onConfirmed.run();
        }, onCancelled);
    }

    private void confirmIfNecessaryAndExecute(boolean needsConfirmation, Message message, Runnable onConfirmed, Runnable onCancelled) {
        if (needsConfirmation) {
            showConfirmationRequest(message, onConfirmed, onCancelled);
        } else {
            onConfirmed.run();
        }
    }

    //todo разобраться с этим диалогом!!! почему view == null
    private void showConfirmationRequest(Message message,
                                         Runnable onConfirmed,
                                         Runnable onCancelled) {

//        ConfirmDialog.createQuestion().withCaption(message.getCaption()).withMessage(message.getMessage())
//                .withOkButton(onConfirmed, ButtonOption.focus(), ButtonOption.caption("Yes"))
//                .withCancelButton(onCancelled)
//                .open();

//                view.getConfirmDialog().open(message.getCaption(), message.getMessage(),"",
//                message.getOkText(), true, state.getEntity(),
//        this::doDelete, onCancelled);
        System.err.println(view == null ? "===============view is null" : "========!!! view is present");
        view.getConfirmDialog().setText(message.getMessage());
        view.getConfirmDialog().setHeader(message.getCaption());
        view.getConfirmDialog().setCancelText(message.getCancelText());
        view.getConfirmDialog().setConfirmText(message.getOkText());
//
        view.getConfirmDialog().open(onConfirmed, onCancelled);
//       // view.getConfirmDialog().setOpened(true);
//
////        final Registration okRegistration = view.getConfirmDialog()
////                .addConfirmListener(e -> onConfirmed.run());
////        final Registration cancelRegistrarion = view.getConfirmDialog()
////                .addCancelListener(e -> onCancelled.run());
//
        final Registration okRegistration = view.getConfirmDialog().getRegistrationForConfirm();
        final Registration cancelRegistration = view.getConfirmDialog().getRegistrationForCancel();

        state.updateRegistration(okRegistration, cancelRegistration);
       // final Registration okRegistration = view.getConfirmDialog().
    }

    private void doDelete(Object entity) {
        crudService.delete(currentUser, (T) entity);
    }

    public boolean loadEntity(Long id, CrudOperationListener<T> onSuccess) {
        return executeOperation(()->{
            state.updateEntity(crudService.load(id), false);
            onSuccess.execute(state.getEntity());
        });
    }

    public T createNew() {
        state.updateEntity(crudService.createNew(currentUser), true);
        return state.getEntity();
    }

    public T getEntity() {
        return state.getEntity();
    }

    public boolean isNew() {
        return state.isNew();
    }

    @FunctionalInterface
    public interface CrudOperationListener<T>{
        void execute(T entity);
    }
}

/**
 * Holds variables that change
 * @param <T>
 */
class EntityPresenterState<T extends AbstractEntity> {

    private T entity;
    private String entityName;
    private Registration okRegistration;
    private Registration cancelRegistration;
    private boolean isNew = false;

    void updateEntity(T entity, boolean isNew) {
        this.entity = entity;
        this.entityName = EntityUtil.getName(this.entity.getClass());
        this.isNew = isNew;
    }

    void updateRegistration(Registration okRegistration, Registration cancelRegistration) {
        clearRegistration(this.okRegistration);
        clearRegistration(this.cancelRegistration);
        this.okRegistration = okRegistration;
        this.cancelRegistration = cancelRegistration;
    }

    private void clearRegistration(Registration registration) {
        if (registration != null) {
            registration.remove();
        }
    }

    void clear() {
        this.entity = null;
        this.entityName = null;
        this.isNew = false;
        updateRegistration(null, null);
    }

    public T getEntity() {
        return entity;
    }

    public String getEntityName(){
        return entityName;
    }

    public boolean isNew(){
        return isNew;
    }


}

