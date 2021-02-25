package tech.itpark.model;

public class RemoveModel {
    private final String login;
    private final String password;

    public RemoveModel(String login, String password, String name) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
