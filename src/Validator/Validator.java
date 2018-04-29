package Validator;

public interface Validator<E> {

    /**
     * Valideaza daca o entitate e valida
     * @param element - elementul de validat
     * @throws ValidationException - daca elementul nu e valid
     */
    void validate(E element) throws ValidationException;
}
