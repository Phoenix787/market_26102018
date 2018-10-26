package ru.xenya.market.backend.data;

import com.vaadin.flow.shared.util.SharedUtil;


/**
 * вид рекламы
 */
public enum Service {
    ADVERTISING {
        @Override
        public String toString() {
            return "Реклама";
        }
    },
    PUBLICATION {
        @Override
        public String toString() {
            return "Публикация";
        }
    },
    ADVERT {
        @Override
        public String toString() {
            return "Объявление";
        }
    },
    CONDOLENCE {
        @Override
        public String toString() {
            return "Соболезнование";
        }
    },
    PROTOTYPE {
        @Override
        public String toString() {
            return "Изготовление макета";
        }
    },
    PREPARATION_TEXT {
        @Override
        public String toString() {
            return "Подготовка информационного материала";
        }
    },
    BOOKING {
        @Override
        public String toString() {
            return "Резервирование места";
        }
    },
    PHOTOGRAPHY {
        @Override
        public String toString() {
            return "Услуги фотосъемки";
        }
    };

    public String getDisplayName(){
        return SharedUtil.capitalize(name().toLowerCase());
    }


}
