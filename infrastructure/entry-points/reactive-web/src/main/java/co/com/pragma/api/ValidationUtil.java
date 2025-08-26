package co.com.pragma.api;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.WebExchangeBindException;

public class ValidationUtil {

    private final Validator validator;

    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T target) {
        var errors = new BeanPropertyBindingResult(target, target.getClass().getName());
        validator.validate(target, errors);
        if (errors.hasErrors()) {
            throw new WebExchangeBindException(null, errors);
        }
    }
}
