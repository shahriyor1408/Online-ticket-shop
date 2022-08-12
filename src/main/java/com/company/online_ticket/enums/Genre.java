package com.company.online_ticket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Genre {
    MUSIC("Music"),
    THEATRE("Theatre"),
    CINEMA("Cinema"),
    CONCERT("Concert"),
    SPORT("Sport");
    private final String key;

    public String getKey() {
        return key;
    }
}
