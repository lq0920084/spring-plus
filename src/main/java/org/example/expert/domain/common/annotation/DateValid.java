package org.example.expert.domain.common.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValid {

    String message() default "8자리의 yyyy-MM-dd형식이어야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] Payload() default {};

    String pattern() default "yyyy-MM-dd";
}
