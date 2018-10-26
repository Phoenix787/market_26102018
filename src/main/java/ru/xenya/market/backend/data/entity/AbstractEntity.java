package ru.xenya.market.backend.data.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@MappedSuperclass
public class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        AbstractEntity that = (AbstractEntity) other;

        return id != null ? id.equals(that.id) : super.equals(that);
    }

    @Override
    public int hashCode() {
        return id != null ? 31 + id.hashCode() : super.hashCode();
    }
}
