package cn.edu.seu.alumni_server.validation.annotaion;


import cn.edu.seu.alumni_server.validation.validator.ValidWebParameterValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidWebParameterValidator.class)
public @interface ValidWebParameter {

	String message() default "当前参数为 null 或空值";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
