package cn.edu.seu.alumni_server.validation.validator;

import cn.edu.seu.alumni_server.validation.annotaion.ValidWebParameter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidWebParameterValidator implements
	ConstraintValidator<ValidWebParameter, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return value != null && !value.equals("");
	}
}
