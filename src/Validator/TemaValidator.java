package Validator;

import Domain.Tema;
import Validator.ValidationException;

public class TemaValidator implements Validator<Tema> {
    @Override
    public void validate(Tema element) throws ValidationException {
        String messages = "";

        if(element.getId() < 0) messages += "Id-ul nu e valid. ";
        if(element.getDescriere() == null || element.getDescriere().equals("")) messages += "Descrierea nu e valida. ";
        if(element.getDeadline() < 1 || element.getDeadline() > 14) messages += "Deadline-ul nu e valid. ";

        if(messages != "") throw new ValidationException(messages);
    }
}
