package Repository;

import Domain.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import Utils.Utils;
import Validator.UserValidator;
import Validator.Validator;

public class UsersRepository extends AbstractFileRepository<String, User> {
    public UsersRepository(String filename) {
        super(new UserValidator(), filename);
    }

    @Override
    protected User createEntity(String[] fields) {
        String username = fields[0];
        String hash = fields[1];
        User.UserType type = User.UserType.valueOf(fields[2]);

        return new User(username, hash, type);
    }
}
