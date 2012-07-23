package com.vlad.validation;

import java.io.Serializable;

public class ValidationError implements Serializable {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    
    public ValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
