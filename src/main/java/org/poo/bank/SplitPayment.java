package org.poo.bank;

import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitPayment {
    private String type;
    private List<String> accounts;
    private double amount;
    private List<Double> amountForUsers;
    private String currency;
    private int timestamp;
    private List<User> users;
    private Map<String, Boolean> userResponses;

    public SplitPayment(CommandInput commandInput, List<User> users) {
        this.type = commandInput.getSplitPaymentType();
        this.accounts = commandInput.getAccounts();
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
}
