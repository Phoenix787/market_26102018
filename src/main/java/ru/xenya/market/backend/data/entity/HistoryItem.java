package ru.xenya.market.backend.data.entity;

import ru.xenya.market.backend.data.OrderState;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class HistoryItem extends AbstractEntity {
    private OrderState newState;

    @NotBlank
    @Size(max = 255)
    private String message;

    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne
    @NotNull
    private User createdBy;

    @java.beans.ConstructorProperties({"newState", "message", "timestamp", "createdBy"})
    public HistoryItem(OrderState newState, @NotBlank @Size(max = 255) String message, @NotNull LocalDateTime timestamp, @NotNull User createdBy) {
        this.newState = newState;
        this.message = message;
        this.timestamp = timestamp;
        this.createdBy = createdBy;
    }

    public HistoryItem() {
    }

    public HistoryItem(User createdBy, String comment) {
        this.createdBy = createdBy;
        this.message = comment;
        this.timestamp = LocalDateTime.now();
    }

    public OrderState getNewState() {
        return this.newState;
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

    public void setNewState(OrderState newState) {
        this.newState = newState;
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof HistoryItem)) return false;
        final HistoryItem other = (HistoryItem) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$newState = this.getNewState();
        final Object other$newState = other.getNewState();
        if (this$newState == null ? other$newState != null : !this$newState.equals(other$newState)) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
        final Object this$timestamp = this.getTimestamp();
        final Object other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !this$timestamp.equals(other$timestamp)) return false;
        final Object this$createdBy = this.getCreatedBy();
        final Object other$createdBy = other.getCreatedBy();
        if (this$createdBy == null ? other$createdBy != null : !this$createdBy.equals(other$createdBy)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $newState = this.getNewState();
        result = result * PRIME + ($newState == null ? 43 : $newState.hashCode());
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        final Object $timestamp = this.getTimestamp();
        result = result * PRIME + ($timestamp == null ? 43 : $timestamp.hashCode());
        final Object $createdBy = this.getCreatedBy();
        result = result * PRIME + ($createdBy == null ? 43 : $createdBy.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof HistoryItem;
    }

    public String toString() {
        return "HistoryItem(newState=" + this.getNewState() + ", message=" + this.getMessage() + ", timestamp=" + this.getTimestamp() + ", createdBy=" + this.getCreatedBy() + ")";
    }
}
