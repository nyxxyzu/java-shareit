package ru.practicum.shareit.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = StartEndDateValidator.class)
public @interface StartBeforeEnd {
	String message() default "Начало бронирования должно быть перед его концом";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
