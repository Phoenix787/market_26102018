package ru.xenya.market.backend.data.entity.util;

import ru.xenya.market.backend.data.entity.AbstractEntity;

public final class EntityUtil {
    public static final String getName(Class<? extends AbstractEntity> type) {
        return type.getSimpleName();
    }
}
