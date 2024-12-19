package org.poo.commands;

import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.NoSuchElementException;

/**
 * Class responsible for setting the minimum balance for an account.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class SetMinBalanceCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public SetMinBalanceCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * This method executes the process of setting the minimum balance for an account.
     */
    @Override
    public void execute() {
        /**
         * get the IBAN of the account
         * aliases have the IBAN of the account and also the aliases of the account as keys for
         * the same IBAN
         */
        String accountIBAN = bank.getAliases().get(command.getAccount());

        // get the account to set the minimum balance
        Account account;
        try {
            account = bank.getAccount(accountIBAN);
        } catch (NoSuchElementException e) {
            return;
        }

        // set the minimum balance for the account
        account.setMinBalance(command.getAmount());
    }
}
