package it.uniroma3.siw.GameHub.controller.validator;

import it.uniroma3.siw.GameHub.model.Credentials;
import it.uniroma3.siw.GameHub.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CredentialsValidator implements Validator {

    @Autowired
    private CredentialsService credentialsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Credentials credentials = (Credentials) target;
        if(credentialsService.existsByUsername(credentials.getUsername())) {
            errors.reject("credentials.username.duplicate");
        }
    }
}
