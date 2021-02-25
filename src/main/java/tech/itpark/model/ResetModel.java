package tech.itpark.model;

public class ResetModel {
    private final String login;
    private final String newPassword;
    private final String secret;

    public ResetModel(String login, String password, String name, String secret) {
        this.login = login;
        this.newPassword = password;
        this.secret = secret;
    }

    public String getLogin() {
        return login;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getSecret() {
        return secret;
    }
}
