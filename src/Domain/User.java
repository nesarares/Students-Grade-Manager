package Domain;

import java.io.Serializable;

public class User implements Serializable, hasId<String> {
    private String username;
    private String hash;
    private UserType type;

    public enum UserType {
        admin, profesor, student
    }

    @Override
    public String getId() {
        return username;
    }

    public User(String username, String hash) {
        this.username = username;
        this.hash = hash;
        this.type = UserType.admin;
    }

    public User(String username, String hash, UserType type) {
        this.username = username;
        this.hash = hash;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "" + username + ";" + hash + ";" + type;
    }
}
