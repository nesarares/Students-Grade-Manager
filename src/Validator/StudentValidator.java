package Validator;

import Domain.Student;
import Utils.Utils;
import Validator.ValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

public class StudentValidator implements Validator<Student> {
    @Override
    public void validate(Student element) throws ValidationException {
        String messages = "";

        if(element.getId() < 0) messages += "Id-ul nu e valid. ";
        if(element.getNume() == null || element.getNume().equals("")) messages += "Numele nu e valid. ";
        if(element.getGrupa() < 0) messages += "Grupa nu e valida. ";
        if(element.getEmail() == null || !Utils.validateEmail(element.getEmail())) messages += "Emailul nu e valid. ";
        if(element.getProf_indrumator() == null || element.getProf_indrumator().equals("")) messages += "Profesorul indrumator nu e vaid.";

        if(!messages.equals("")) throw new ValidationException(messages);
    }
}
