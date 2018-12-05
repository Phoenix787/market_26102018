package ru.xenya.market.backend.data.entity;

import javax.persistence.*;
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
        if (id == null){
            return super.equals(other);
        }

        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractEntity)) {
            return false;
        }
        return id.equals(((AbstractEntity) other).id);
    }

    @Override
    public int hashCode() {
        return id != null ? 31 + id.hashCode() : super.hashCode();
    }
}
