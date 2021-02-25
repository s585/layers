package tech.itpark.model;

public class AuthentificationModel {
    private final String login;
    private final String password;

    public AuthentificationModel(String login, String password, String name) {
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
