package ru.xenya.market.backend.data;

import com.vaadin.flow.shared.util.SharedUtil;

public enum OrderState {
    NEW {
        @Override
        public String toString() {
            return "Новый";
        }

    },
    CONFIRMED{
        @Override
        public String toString() {
            return "Подтвержденный";
        }
    },
    READY{
        @Override
        public String toString() {
            return "Готовый";
        }
    },
    PROBLEM {
        @Override
        public String toString() {
            return "Затруднение";
        }
    },
    CANCELED {
        @Override
        public String toString() {
            return "Отменён";
        }
    };

    public String getDisplayName(){
        return SharedUtil.capitalize(name().toLowerCase());
    }


}
