package org.poo.bank.account;

import org.poo.fileio.CommandInput;

public final class AccountFactory {
    private AccountFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static Account createAccount(final CommandInput command) {
        String accountType = command.getAccountType();

        return switch (accountType) {
            case "savings" -> new SavingsAccount(command);
            case "classic" -> new ClassicAccount(command);
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        };
    }
}
