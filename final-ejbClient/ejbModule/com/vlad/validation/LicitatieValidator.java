package com.vlad.validation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.vlad.dto.LicitatieDTO;

public class LicitatieValidator {
    
    private static final Logger logger = Logger.getLogger(LicitatieValidator.class);

    public static List<ValidationError> validate(LicitatieDTO licitatie) {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (licitatie == null) {
            logger.error("Licitatia trimisa la validare a fost null");
        } else {
            Date now = new Date();
            if (licitatie.getValoareStart() == 0) {
                errors.add(new ValidationError("Valoarea de start pentru o licitatie nu poate fi zero"));
            }
            if (licitatie.getValoareCrestere() == 0) {
                errors.add(new ValidationError("Valoarea de crestere pentru o licitatie nu poate fi zero"));
            }
            if (licitatie.getTimpDeschidere() == null) {
                errors.add(new ValidationError("Timpul de deschidere nu poate sa lipseasca"));
            } else if (licitatie.getTimpInchidere() == null) {
                errors.add(new ValidationError("Timpul de inchidere nu poate sa lipseasca"));
            } else if (licitatie.getTimpDeschidere().getTime() < now.getTime()) {
                errors.add(new ValidationError("Timpul de deschidere trebuie sa fie in viitor"));
            } else if (licitatie.getTimpInchidere().getTime() < now.getTime()) {
                errors.add(new ValidationError("Timpul de inchidere trebuie sa fie in viitor"));
            } else if (licitatie.getTimpDeschidere().getTime() > licitatie.getTimpInchidere().getTime()) {
                errors.add(new ValidationError("Timpul de inchidere trebuie sa fie dupa timpul de deschidere"));
            }
        }
        return errors;
    }

}
