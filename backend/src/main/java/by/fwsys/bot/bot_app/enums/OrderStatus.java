package by.fwsys.bot.bot_app.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED", "создан"),
    WAITING("WAITING", "ожидание"),
    DISTRIBUTION("DISTRIBUTION", "на распределении"),
    ADMIN_REVIEW("ADMIN_REVIEW", "на проверке админа"),
    IN_PROGRESS("IN_PROGRESS", "в работе"),
    REVIEW("REVIEW", "проверка"),
    GUARANTEE("GUARANTEE", "на гарантии"),
    FINALIZATION("FINALIZATION", "доработка"),
    CLOSED("CLOSED", "завершен"),

    ;

    private final String name;

    private final String rusName;

    OrderStatus(String name, String rusName) {
        this.name = name;
        this.rusName = rusName;
    }
}
