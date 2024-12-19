package org.poo.bank.accounts;

import org.poo.fileio.CommandInput;

/**
 * Factory class to create an account.
 */
public final class AccountFactory {
    /**
     * Private constructor to prevent instantiation.
     * @throws UnsupportedOperationException if the constructor is called
     */
    private AccountFactory() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Create an account based on the command input.
     * @param command the command input that contains the account type and other information
     *                about the account to be created
     * @return the account created, either a SavingsAccount or a ClassicAccount
     */
    public static Account createAccount(final CommandInput command) {
        String accountType = command.getAccountType();

        return switch (accountType) {
            case "savings" -> new SavingsAccount(command);
            case "classic" -> new ClassicAccount(command);
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        };
    }
}
