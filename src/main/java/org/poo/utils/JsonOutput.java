package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.commerciants.Commerciant;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Class responsible for writing the output in JSON format.
 */
public final class JsonOutput {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonOutput() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Method responsible for writing the users in JSON format.
     * @param command the command input that contains information about the command
     * @param bank the bank that contains the user data
     * @return the users as a JSON object
     */
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

    /**
     * Helper method responsible for writing one user in JSON format.
     * @param user the user to be written
     * @return the user as a JSON object
     */
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

    /**
     * Helper method responsible for writing an account in JSON format.
     * @param account the account to be written
     * @return the account as a JSON object
     */
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

    /**
     * Helper method responsible for writing a card in JSON format.
     * @param card the card to be written
     * @return the card as a JSON object
     */
    private static ObjectNode writeCard(final Card card) {
        ObjectNode cardNode = MAPPER.createObjectNode();

        cardNode.put("cardNumber", card.getNumber());
        cardNode.put("status", card.getStatus());

        return cardNode;
    }

    /**
     * Method responsible for writing an error message when an account is not found.
     * @param command  the command input that contains information about the command
     * @return the error message as a JSON object
     */
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

    /**
     * Method responsible for writing a JSON output for deleting an account. If the account has a
     * balance different from 0,an error message is added to the output.
     * @param command the command input that contains information about the command
     * @param account the account to be deleted
     * @return the output as a JSON object
     */
    public static ObjectNode eraseAccount(final CommandInput command, final Account account) {
        ObjectNode deleteAccount = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        deleteAccount.put("command", command.getCommand());

        // check if the account has a balance different from 0
        // if it has, add an error message to the output
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

    /**
     * Method responsible for writing an error message when a card is not found.
     * @param command the command input that contains information about the command
     * @return the error message as a JSON object
     */
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

    /**
     * Method responsible for writing an error message when a user is not found.
     * @param command the command input that contains information about the command
     * @return the error message as a JSON object
     */
    public static ObjectNode userNotFound(final CommandInput command) {
        ObjectNode errorUser = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        errorUser.put("command", command.getCommand());

        output.put("timestamp", command.getTimestamp());
        output.put("description", "User not found");

        errorUser.set("output", output);
        errorUser.put("timestamp", command.getTimestamp());

        return errorUser;
    }

    /**
     * Method responsible for writing a JSON output for writing transactions.
     * @param commandInput the command input that contains information about the command
     * @param user the user that contains the transactions
     * @return the transactions as a JSON object
     */
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

    /**
     * Helper method responsible for writing a transaction in JSON format.
     * @param transaction the transaction to be written
     * @return the transaction as a JSON object
     */
    private static ObjectNode writeTransaction(final Transaction transaction) {
        return MAPPER.valueToTree(transaction);
    }

    /**
     * Method responsible for writing an error message when account is savings.
     * @param command the command input that contains information about the command
     * @return the error message as a JSON object
     */
    public static ObjectNode writeErrorSavingAccount(final CommandInput command) {
        ObjectNode errorSavingAccount = MAPPER.createObjectNode();
        ObjectNode output = MAPPER.createObjectNode();

        errorSavingAccount.put("command", command.getCommand());

        output.put("timestamp", command.getTimestamp());
        output.put("description", "Account is not of type savings.");

        errorSavingAccount.set("output", output);
        errorSavingAccount.put("timestamp", command.getTimestamp());

        return errorSavingAccount;
    }

    /**
     * Method responsible for writing a JSON output for a classic report.
     * @param command the command input that contains information about the command
     * @param account the account that contains the transactions
     * @param transactions the transactions to be written
     * @return the classic report as a JSON object
     */
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

    /**
     * Method responsible for writing a JSON output for a spending report.
     * @param command the command input that contains information about the command
     * @param account the account that contains the transactions
     * @param transactions the transactions to be written
     * @param commerciants the commerciants that received money
     * @return the spending report as a JSON object
     */
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

        // write the commerciants that received money
        for (Commerciant commerciant : commerciants.values()) {
            ObjectNode commerciantJson = MAPPER.createObjectNode();

            commerciantJson.put("commerciant", commerciant.getCommerciant());
            commerciantJson.put("total", commerciant.getMoneyReceived());

            commerciansArray.add(commerciantJson);
        }

        spendingDetails.set("commerciants", commerciansArray);

        report.set("output", spendingDetails);
        report.put("timestamp", command.getTimestamp());

        return report;
    }

    /**
     * Method responsible for writing an error message when an account is not eligible for a
     * spending report.
     * @param command the command input that contains information about the command
     * @return the error message as a JSON object
     */
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
