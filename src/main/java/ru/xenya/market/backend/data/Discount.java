package ru.xenya.market.backend.data;

public enum Discount {
    none {
        @Override
        public String toString() {
            return "нет";
        }
    },
    ten{
        @Override
        public String toString() {
            return "10%";
        }
    },
    twenty{
        @Override
        public String toString() {
            return "20%";
        }
    },
    thirty{
        @Override
        public String toString() {
            return "30%";
        }
    };
}
