package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.commands.*;
import org.poo.fileio.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class representing a bank and its database of different elements (users, accounts, cards etc.).
 */
@Setter
@Getter
public final class Bank {
    private Map<String, User> users = new LinkedHashMap<>();
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, Card> cards = new HashMap<>();
    private ExchangeRates exchangeRates = new ExchangeRates();
    private Map<String, String> aliases = new HashMap<>();
    private ArrayNode output;

    public Bank(final ObjectInput input, final ArrayNode output) {
        for (UserInput userInput : input.getUsers()) {
            User user = new User(userInput);
            users.put(user.getEmail(), user);
        }

        for (ExchangeInput exchangeInput : input.getExchangeRates()) {
            exchangeRates.addRate(exchangeInput.getFrom(), exchangeInput.getTo(),
                                    exchangeInput.getRate());
        }
        this.output = output;
    }

    /**
     * Method that finds a user based on their email. If the user is not found, a
     * NoSuchElementException is thrown. This exception is caught in the command classes when we
     * are looking for a user.
     * @param email the email of the user as every user has a unique email.
     * @return the user with the given email.
     */
    public User getUser(final String email) {
        if (!users.containsKey(email)) {
            throw new NoSuchElementException("User not found");
        }
        return users.get(email);
    }

    /**
     * Method that finds an account based on its IBAN. If the account is not found, a
     * NoSuchElementException is thrown. This exception is caught in the command classes when we
     * are looking for an account.
     * @param iban the IBAN of the account as every account has a unique IBAN.
     * @return the account with the given IBAN.
     */
    public Account getAccount(final String iban) {
        if (!accounts.containsKey(iban)) {
            throw new NoSuchElementException("Account not found");
        }
        return accounts.get(iban);
    }

    /**
     * Method that finds a card based on its card number. If the card is not found, a
     * NoSuchElementException is thrown. This exception is caught in the command classes when we
     * are looking for a card.
     * @param cardNumber the card number of the card as every card has a unique card number.
     * @return the card with the given card number.
     */
    public Card getCard(final String cardNumber) {
        if (!cards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Card not found");
        }
        return cards.get(cardNumber);
    }

    /**
     * Method that executes a command given in the input. This is part of the command pattern.
     * @param input the command input to be performed.
     */
    public void performCommand(final CommandInput input) {
        Command command = createCommand(input);
        if (command != null) {
            command.execute();
        }
    }

    /**
     * Method that creates a command based on the command input. This is part of the command
     * pattern. The command is created based on the command input and the command is then executed.
     * @param input the command input to be performed.
     * @return the command to be executed.
     */
    private Command createCommand(final CommandInput input) {
        return switch (input.getCommand()) {
            case "printUsers" -> new PrintUsersCommand(this, input, output);
            case "addAccount" -> new AddAccountCommand(this, input);
            case "addFunds" -> new AddFundsCommand(this, input);
            case "createCard", "createOneTimeCard" -> new CreateCardCommand(this, input);
            case "deleteAccount" -> new DeleteAccountCommand(this, input, output);
            case "deleteCard" -> new DeleteCardCommand(this, input);
            case "setMinBalance" -> new SetMinBalanceCommand(this, input);
            case "payOnline" -> new PayOnlineCommand(this, input, output);
            case "sendMoney" -> new SendMoneyCommand(this, input);
            case "printTransactions" -> new PrintTransactionsCommand(this, input, output);
            case "setAlias" -> new SetAliasCommand(this, input);
            case "checkCardStatus" -> new CheckCardStatusCommand(this, input, output);
            case "splitPayment" -> new SplitPaymentCommand(this, input);
            case "addInterest" -> new AddInterestCommand(this, input, output);
            case "changeInterestRate" -> new ChangeInterestRateCommand(this, input, output);
            case "report", "spendingsReport" -> new MakeReportCommand(this, input, output);
            default -> null;
        };
    }
}
