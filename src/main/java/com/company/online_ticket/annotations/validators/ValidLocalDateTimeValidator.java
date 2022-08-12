package com.company.online_ticket.annotations.validators;

import com.company.online_ticket.annotations.ValidLocalDateTime;
import com.company.online_ticket.util.Utils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Clock;
import java.time.LocalDateTime;

public class ValidLocalDateTimeValidator implements ConstraintValidator<ValidLocalDateTime, String> {
    private String message;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Utils.isParsable(value)) {
            LocalDateTime localDateTime = Utils.toLocalDateTime(value);
            return !localDateTime.isBefore(LocalDateTime.now(Clock.systemDefaultZone()));
        }
        return !StringUtils.hasText(value);
    }

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }
}
