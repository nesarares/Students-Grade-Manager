package Service;

import Domain.Student;
import Domain.User;
import Repository.Repository;
import Repository.RepositoryException;
import Utils.*;
import Validator.ValidationException;

import java.util.List;
import java.util.Optional;

public class UserService extends AbstractService<String, User> implements Observer<Student> {

    public UserService(Repository<String, User> repository) {
        super(repository);
    }

    public void addUser(String user, String password, User.UserType type) throws ValidationException, RepositoryException {
        String msg = "";

        if (user == null || user.equals(""))
            msg += "Userul este invalid.";
        if (type == null)
            msg += "Tipul este invalid.";
        if (password == null || password.equals(""))
            msg += "Parola este invalida.";

        if (! msg.equals(""))
            throw new ValidationException(msg);

        if (! repository.find(user).isPresent())
            try {
                String hash = PasswordStorage.createHash(password);
                add(new User(user, hash, type));
            } catch (PasswordStorage.CannotPerformOperationException e) {
                System.out.println("Nu s-a putut adauga un utilizator.");
            }
    }

    public void addUser(String user, User.UserType type) throws ValidationException, RepositoryException {
        addUser(user, Utils.defaultPassword, type);
    }

    public void modifyUser(String user, String oldPassword, String newPassword, String newPasswordRetype) throws ValidationException {
        String msg = "";

        if (newPassword.length() < 5)
            msg += "Parola trebuie sa contina cel putin 5 caractere. ";
        if (! newPassword.equals(newPasswordRetype))
            msg += "Parolele nu coincid. ";
        if (! verifyPassword(user, oldPassword)) {
            msg += "Parola veche nu este corecta. ";
        }

        if (! msg.equals(""))
            throw new ValidationException(msg);

        String hash = null;
        try {
            hash = PasswordStorage.createHash(newPassword);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            throw new ValidationException("Nu s-a putut realiza modificarea.");
        }

        Optional<User> userFound = repository.find(user);
        if (!userFound.isPresent())
            throw (new ValidationException("Userul " + user + " nu exista."));

        update(new User(user, hash, userFound.get().getType()));
    }

    public boolean verifyPassword(String username, String password) {
        Optional<User> user = repository.find(username);

        if (user.isPresent()) {
            String userHash = user.get().getHash();

            boolean result = false;
            try {
                result = PasswordStorage.verifyPassword(password, userHash);
            } catch (PasswordStorage.CannotPerformOperationException e) {
                System.out.println("Nu s-a putut realiza verificarea.");
            } catch (PasswordStorage.InvalidHashException e) {
                System.out.println("User invalid in memorie.");
            }

            return result;
        }
        else
            return false;
    }

    public User.UserType getUserType(String user) {
        return repository.find(user).get().getType();
    }

    @Override
    public void notifyEvent(ListEvent<Student> event) {
        Student addedStudent = event.getElement();
        if (event.getType() == ListEventType.ADD) {
            try {
                addUser(addedStudent.getUsername(), Utils.defaultPassword, User.UserType.student);
            } catch (Exception e) {
                System.out.println("Nu s-a putut adauga utilizatorul pentru studentul " + addedStudent.getNume() + ".");
            }
        } else if (event.getType() == ListEventType.REMOVE) {
            remove(addedStudent.getUsername());
        }
    }

    public void resetPassword(User user) throws ValidationException {
        try {
            String newHash = PasswordStorage.createHash(Utils.defaultPassword);
            user.setHash(newHash);
            repository.update(user);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            System.out.println("Nu s-a putut reseta parola utilizatorului " + user.getUsername() + ".");
        }
    }

    public List<User> filtreazaUseriKeyword(String keyword, List<User> list) {
        if (keyword.toLowerCase().startsWith("user:")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String user = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getUsername()).toLowerCase().contains(user), null);
            }
        }
        else if (keyword.toLowerCase().startsWith("tip:")) {
            String[] fields = keyword.split(":");
            if(fields.length > 1) {
                String tip = fields[1].trim().toLowerCase();
                return Filter.filterAndSorter(list, entity -> String.valueOf(entity.getType()).toLowerCase().contains(tip), null);
            }
        }

        return Filter.filterAndSorter(list,
                entity -> String.valueOf(entity.getUsername()).contains(keyword.toLowerCase()) ||
                        String.valueOf(entity.getType()).toLowerCase().contains(keyword.toLowerCase()),
                null);
    }
}
