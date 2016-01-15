package org.books.application.interceptor;

import org.books.application.exception.ValidationException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.util.Set;
import java.util.logging.Logger;

public class ValidationInterceptor {

	private static final Logger logger = Logger.getLogger(ValidationInterceptor.class.getName());
	private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	@AroundInvoke
	public Object validate(InvocationContext ic) throws Exception {
		if (ic.getParameters() != null) {
			ExecutableValidator validator = factory.getValidator().forExecutables();
			Set violations = validator.validateParameters(ic.getTarget(), ic.getMethod(), ic.getParameters());
			if (!violations.isEmpty()) {
				logger.warning(violations.toString());
				throw new ValidationException(createMessage(violations));
			}
		}
		return ic.proceed();
	}

	private String createMessage(Set<ConstraintViolation> violations) {
		String message = "";
		for (ConstraintViolation violation : violations) {
			message += "\t" + violation.getRootBeanClass().getSimpleName() + "."
					+ violation.getPropertyPath() + ": " + violation.getMessage() + "\n";
		}
		return message;
	}
}
