package by.fwsys.bot.bot_app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InformationLevel {
    INFO("ℹ️"),
    WARN("⚠️"),
    DANGER("🚨"),
    ;

    private final String icon;
}
