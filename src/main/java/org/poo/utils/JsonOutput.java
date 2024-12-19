package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.Commerciant;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.List;
import java.util.Map;

public final class JsonOutput {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonOutput() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static ObjectNode writeUsers(final CommandInput command, final Bank bank) {
        ObjectNode usersArray = MAPPER.createObjectNode();

        usersArray.put("command", command.getCommand());

        ArrayNode users = MAPPER.createArrayNode();
        for (User user : bank.getUsers().values()) {
            ObjectNode userNode = writeOneUser(user);
            users.add(userNode);
        }

        usersArray.set("output", users);
        usersArray.put("timestamp", command.getTimestamp());
        return usersArray;
    }

    private static ObjectNode writeOneUser(final User user) {
        ObjectNode userNode = MAPPER.createObjectNode();

        userNode.put("firstName", user.getFirstName());
        userNode.put("lastName", user.getLastName());
        userNode.put("email", user.getEmail());

        ArrayNode accountsArray = MAPPER.createArrayNode();

        for (Account account : user.getAccounts()) {
            ObjectNode accountNode = writeAccount(account);
            accountsArray.add(accountNode);
        }

        userNode.set("accounts", accountsArray);
        return userNode;
    }

    private static ObjectNode writeAccount(final Account account) {
        ObjectNode accountNode = MAPPER.createObjectNode();

        accountNode.put("IBAN", account.getIban());
        accountNode.put("balance", account.getBalance());
        accountNode.put("currency", account.getCurrency());
        accountNode.put("type", account.getAccountType());

        ArrayNode cardsArray = MAPPER.createArrayNode();
        for (Card card : account.getCards()) {
            ObjectNode cardNode = writeCard(card);
            cardsArray.add(cardNode);
        }

        accountNode.set("cards", cardsArray);
        return accountNode;
    }

    public static ObjectNode accountNotFound(final CommandInput command) {
        ObjectNode errorAccount = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        errorAccount.put("command", command.getCommand());

        output.put("timestamp", command.getTimestamp());
        output.put("description", "Account not found");

        errorAccount.set("output", output);
        errorAccount.put("timestamp", command.getTimestamp());

        return errorAccount;
    }

    private static ObjectNode writeCard(final Card card) {
        ObjectNode cardNode = MAPPER.createObjectNode();

        cardNode.put("cardNumber", card.getNumber());
        cardNode.put("status", card.getStatus());

        return cardNode;
    }

    public static ObjectNode eraseAccount(final CommandInput command, final Account account) {
        ObjectNode deleteAccount = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        deleteAccount.put("command", command.getCommand());


        if (account.getBalance() != 0) {
            output.put("error", "Account couldn't be deleted - see org.poo.transactions for "
                    + "details");
            output.put("timestamp", command.getTimestamp());
        } else {
            output.put("success", "Account deleted");
            output.put("timestamp", command.getTimestamp());
        }

        deleteAccount.set("output", output);
        deleteAccount.put("timestamp", command.getTimestamp());

        return deleteAccount;
    }

    public static ObjectNode cardNotFound(final CommandInput command) {
        ObjectNode errorCard = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        errorCard.put("command", command.getCommand());

        output.put("timestamp", command.getTimestamp());
        output.put("description", "Card not found");

        errorCard.set("output", output);
        errorCard.put("timestamp", command.getTimestamp());

        return errorCard;
    }

    public static ObjectNode writeTransactions(final CommandInput commandInput, final User user) {
        ObjectNode transactionsOutput = MAPPER.createObjectNode();

        transactionsOutput.put("command", commandInput.getCommand());

        ArrayNode transactionsArray = MAPPER.createArrayNode();

        for (Transaction transaction : user.getTransactions()) {
            ObjectNode transactionJson = writeTransaction(transaction);
            transactionsArray.add(transactionJson);
        }

        transactionsOutput.set("output", transactionsArray);
        transactionsOutput.put("timestamp", commandInput.getTimestamp());

        return  transactionsOutput;
    }

    public static ObjectNode writeTransaction(final Transaction transaction) {
        return MAPPER.valueToTree(transaction);
    }

    public static ObjectNode writeErrorSavingAccount(final CommandInput command) {
        ObjectNode errorSavingAccount = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        errorSavingAccount.put("command", command.getCommand());

        output.put("timestamp", command.getTimestamp());
        output.put("description", "This is not a savings account");

        errorSavingAccount.set("output", output);
        errorSavingAccount.put("timestamp", command.getTimestamp());

        return errorSavingAccount;
    }

    public static ObjectNode writeClassicReport(final CommandInput command, final Account account,
                                                final List<Transaction> transactions) {
        ObjectNode report = MAPPER.createObjectNode();
        report.put("command", command.getCommand());

        ObjectNode accountDetails = MAPPER.createObjectNode();
        accountDetails.put("IBAN", account.getIban());
        accountDetails.put("balance", account.getBalance());
        accountDetails.put("currency", account.getCurrency());

        ArrayNode transactionsArray = MAPPER.createArrayNode();
        for (Transaction transaction : transactions) {
            ObjectNode transactionJson = writeTransaction(transaction);
            transactionsArray.add(transactionJson);
        }

        accountDetails.set("transactions", transactionsArray);

        report.set("output", accountDetails);
        report.put("timestamp", command.getTimestamp());

        return report;
    }

    public static ObjectNode writeSpendingReport(final CommandInput command, final Account account,
                                                 final List<Transaction> transactions,
                                                 final Map<String, Commerciant> commerciants) {
        ObjectNode report = MAPPER.createObjectNode();
        report.put("command", command.getCommand());

        ObjectNode spendingDetails = MAPPER.createObjectNode();
        spendingDetails.put("IBAN", account.getIban());
        spendingDetails.put("balance", account.getBalance());
        spendingDetails.put("currency", account.getCurrency());

        ArrayNode transactionsArray = MAPPER.createArrayNode();
        for (Transaction transaction : transactions) {
            ObjectNode transactionJson = writeTransaction(transaction);
            transactionsArray.add(transactionJson);
        }

        spendingDetails.set("transactions", transactionsArray);

        ArrayNode commerciansArray = MAPPER.createArrayNode();

        for (Commerciant commerciant : commerciants.values()) {
            ObjectNode commerciantJson = MAPPER.createObjectNode();

            commerciantJson.put("commerciant", commerciant.getName());
            commerciantJson.put("total", commerciant.getMoneyReceived());

            commerciansArray.add(commerciantJson);
        }

        spendingDetails.set("commerciants", commerciansArray);

        report.set("output", spendingDetails);
        report.put("timestamp", command.getTimestamp());

        return report;
    }

    public static ObjectNode accountNotEligible(final CommandInput command) {
        ObjectNode errorAccount = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        errorAccount.put("command", command.getCommand());

        output.put("error", "This kind of report is not supported for a saving account");

        errorAccount.set("output", output);
        errorAccount.put("timestamp", command.getTimestamp());

        return errorAccount;
    }
}
