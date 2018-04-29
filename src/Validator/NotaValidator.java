package Validator;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.Repository;
import Utils.Utils;

public class NotaValidator implements Validator<Nota> {
    public static void validate(Nota element, Repository<Integer, Student> repository_student,
                         Repository<Integer, Tema> repository_tema) throws ValidationException {
        if(! repository_student.find(element.getId_student()).isPresent())
            throw(new ValidationException("Nu exista studentul cu acel id."));
        if(! repository_tema.find(element.getId_tema()).isPresent())
            throw(new ValidationException("Nu exista tema cu acel id."));

        validateAux(element);
    }

    private static void validateAux(Nota element) throws ValidationException {
        String messages = "";

        if(element.getId() < 0)
            messages += "Id-ul nu este valid. ";
        if(element.getNota() < 1 || element.getNota() > 10)
            messages += "Nota nu este corecta. ";
        if(element.getSaptamana_predare() < 1 || element.getSaptamana_predare() > Utils.getCurrentWeek())
            messages += "Saptamana nu este corecta.";

        if(!messages.equals("")) throw new ValidationException(messages);
    }

    @Override
    public void validate(Nota element) throws ValidationException {
        String messages = "";

        if(element.getId() < 0)
            messages += "Id-ul nu este valid. ";
        if(element.getNota() < 1 || element.getNota() > 10)
            messages += "Nota nu este corecta. ";
        if(element.getSaptamana_predare() < 1 || element.getSaptamana_predare() > Utils.getCurrentWeek())
            messages += "Saptamana nu este corecta.";

        if(!messages.equals("")) throw new ValidationException(messages);
    }
}
