package com.example.web.anno;

import com.example.web.validated.StateValidated;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Constraint(
        validatedBy = {StateValidated.class}
)

@Retention(RetentionPolicy.RUNTIME)

public @interface State {
    String message() default "文章状态只能是： 已发布或者草稿";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
