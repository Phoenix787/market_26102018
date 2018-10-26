package ru.xenya.market.backend.data;

/**
 * единицы измерения
 */
public enum Unit {
    piece{
        @Override
        public String toString() {
            return "шт.";
        }
    },
    cm2{
        @Override
        public String toString() {
            return "см2";
        }
    },
    word{
        @Override
        public String toString() {
            return "слово";
        }
    },
    sign {
        @Override
        public String toString() {
            return "знак";
        }
    },
    hour {
        @Override
        public String toString() {
            return "час";
        }
    };
}
