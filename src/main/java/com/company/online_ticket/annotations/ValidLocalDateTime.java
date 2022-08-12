package com.company.online_ticket.annotations;


import com.company.online_ticket.annotations.validators.ValidLocalDateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ValidLocalDateTimeValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ValidLocalDateTime {

    String message() default "Field must be valid to date time";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}

