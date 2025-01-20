package org.poo.bank;

import lombok.Getter;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SplitPayment {
    private static final String SPLIT_PAYMENT_MESSAGE = "Split payment of %.2f %s";
    private final String type;
    private final List<Account> accounts;
    private final double amount;
    private final List<Double> amountForUsers;
    private final String currency;
    private final int timestamp;
    private final List<User> users;
    private final Map<String, Boolean> userResponses;

    public SplitPayment(CommandInput commandInput, List<Account> accounts, List<User> users) {
        this.type = commandInput.getSplitPaymentType();
        this.accounts = accounts;
        this.amount = commandInput.getAmount();
        this.amountForUsers = commandInput.getAmountForUsers();
        this.currency = commandInput.getCurrency();
        this.timestamp = commandInput.getTimestamp();
        this.users = users;
        this.userResponses = new HashMap<>();
        for (User user : users) {
            userResponses.put(user.getEmail(), null);
        }
    }

    public void process(final Bank bank) {
        String error = null;

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);

            double amountRequired;
            if (type.equals("equal")) {
                amountRequired = amount / accounts.size();
            } else {
                amountRequired = amountForUsers.get(i);
            }

            double exchangeRate = bank.getExchangeRates().getRate(currency, account.getCurrency());

            if (!account.checkEnoughMoney(amountRequired * exchangeRate)) {
                error = "Account " + account.getIban() + " has insufficient funds for a split payment.";
                break;
            }
        }

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            Double amountTransaction = null;

            double amountRequired;
            if (type.equals("equal")) {
                amountRequired = amount / accounts.size();
                amountTransaction = amountRequired;
            } else {
                amountRequired = amountForUsers.get(i);
            }

            double exchangeRate = bank.getExchangeRates().getRate(currency, account.getCurrency());

            if (error == null) {
                account.withdraw(amountRequired * exchangeRate);
            }

            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                    // I do like this as ref has amount with 2 decimals even if it is an int
                    // (1269.00 RON)
                    String.format(SPLIT_PAYMENT_MESSAGE, amount, currency))
                    .currency(currency)
                    .involvedAccounts(getAccountsIbans())
                    .amount(amountTransaction)
                    .amountForUsers(amountForUsers)
                    .splitPaymentType(type)
                    .error(error)
                    .build();

            account.getOwner().addTransaction(transaction);
            account.addTransaction(transaction);
        }
    }

    public void clearForAllUsers() {
        for (User user : users) {
            user.removePendingPayment(this);
        }
    }

    private List<String> getAccountsIbans() {
        return accounts.stream()
                .map(Account::getIban)
                .toList();
    }
}
