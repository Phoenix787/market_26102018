package ru.xenya.market.backend.data.entity;

import ru.xenya.market.backend.data.OrderState;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * класс для истории изменений прайса
 */
@Entity
public class HistoryItem extends AbstractEntity {

    @NotBlank
    @Size(max = 255)
    private String message;

    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne
    @NotNull
    private User createdBy;

    public HistoryItem() {
    }

    public HistoryItem(User createdBy, String comment) {
        this.createdBy = createdBy;
        this.message = comment;
        this.timestamp = LocalDateTime.now();
    }

    public @NotBlank @Size(max = 255) String getMessage() {
        return this.message;
    }

    public @NotNull LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public @NotNull User getCreatedBy() {
        return this.createdBy;
    }

    public void setMessage(@NotBlank @Size(max = 255) String message) {
        this.message = message;
    }

    public void setTimestamp(@NotNull LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setCreatedBy(@NotNull User createdBy) {
        this.createdBy = createdBy;
    }


}
