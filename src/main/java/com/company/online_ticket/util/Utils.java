package com.company.online_ticket.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Utils {

    public static boolean isParsable(String timeAsString) {
        try {
            LocalDateTime.parse(timeAsString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDateTime toLocalDateTime(String timeAsString) {
        try {
            return LocalDateTime.parse(timeAsString);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
