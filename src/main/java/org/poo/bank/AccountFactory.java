package org.poo.bank;

import org.poo.fileio.CommandInput;

public class AccountFactory {
    public static Account createAccount(CommandInput command) {
        String accountType = command.getAccountType();

        return switch (accountType) {
            case "savings" -> new SavingsAccount(command);
            case "classic" -> new ClassicAccount(command);
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        };
    }
}
