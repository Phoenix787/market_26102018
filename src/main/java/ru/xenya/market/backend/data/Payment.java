package ru.xenya.market.backend.data;

public enum Payment {
    CASH{
        @Override
        public String toString() {
            return "Наличная";
        }
    },
    NONCASH{
        @Override
        public String toString() {
            return "Безналичная";
        }
    };
}
