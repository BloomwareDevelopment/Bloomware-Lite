package me.offeex.bloomware.api.alts;

public class Alt {
    private String login, password;
    private final AltType type;

    public Alt(String login, String password, AltType type) {
        this.login = login;
        this.password = password;
        this.type = type;
    }

    public void setLogin(String newLogin) {
        this.login = newLogin;
    }

    public void setPassword(String newPassword) {
        this.password = password;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public AltType getType() {
        return this.type;
    }

    public enum AltType {
        MICROSOFT, CRACKED, MOJANG
    }
}
