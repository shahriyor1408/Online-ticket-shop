package com.company.online_ticket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusHistory {
    NOT_ACCEPTED("NOT_ACCEPTED"),
    ACCEPTED("ACCEPTED");

    public final String key;
}
