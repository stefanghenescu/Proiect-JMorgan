package org.poo.commands;

import org.poo.bank.SplitPayment;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class responsible for splitting a payment between multiple accounts.
 * Implements the Command interface. This class is part of the Command design pattern.
 */
public final class SplitPaymentCommand implements Command {
    private final Bank bank;
    private final CommandInput command;

    public SplitPaymentCommand(final Bank bank, final CommandInput command) {
        this.bank = bank;
        this.command = command;
    }

    /**
     * Method responsible for splitting a payment between multiple accounts.
     * A split payment is created and added to the pending payments of the users.
     */
    @Override
    public void execute() {
        List<String> accountIBANs = command.getAccounts();
        List<Account> accounts = new ArrayList<>();
        List<User> users = new ArrayList<>();

        // for every iban of the accounts, get the account and the user
        // instantiate a split payment and add it to the pending payments of the users
        for (String iban : accountIBANs) {
            Account account;
            User user;

            try {
                account = bank.getAccount(iban);
            } catch (NoSuchElementException e) {
                return;
            }
            accounts.add(account);

            try {
                user = account.getOwner();
            } catch (NoSuchElementException e) {
                return;
            }
            users.add(user);
        }

        SplitPayment splitPayment = new SplitPayment(command, accounts, users);

        for (User user : users) {
            user.addPendingPayment(splitPayment);
        }
    }
}
