package me.offeex.bloomware.api.serveraccount;

import java.util.ArrayList;
import java.util.Random;

public class ServerAccountManager {
    private final ArrayList<ServerAccount> accounts = new ArrayList<>();
    private static final Random random = new Random();

    public void addAccount(ServerAccount account) {
        accounts.add(account);
    }

    public void save() {}

    public void load() {}

    public ServerAccount findAccount(String login, String server) {
        return accounts.stream().filter(account -> account.server().equals(server) && account.nickname().equals(login)).findAny().orElse(null);
    }

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        for (byte i = 0; i < 10; i++) password.append((char) (random.nextInt(26) + 'a'));
        return password.toString();
    }
}
