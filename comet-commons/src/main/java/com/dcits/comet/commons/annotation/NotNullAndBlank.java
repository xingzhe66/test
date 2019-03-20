package com.dcits.comet.commons.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/***
 * @description
 * @version V1.0
 * @author ChengLiang
 * @date 2019/3/19
 */
@NotNull
@NotBlank
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface NotNullAndBlank {
    String message() default "必须不为null且不为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
