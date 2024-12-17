package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.account.Account;
import org.poo.bank.cards.Card;
import org.poo.commands.*;
import org.poo.fileio.*;

import java.util.*;

@Setter
@Getter
public class Bank {
    private Map<String, User> users = new LinkedHashMap<>();
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, Card> cards = new HashMap<>();
    private ExchangeRates exchangeRates = new ExchangeRates();
    private Map<String, String> aliases = new HashMap<>();
    private ArrayNode output;

    public Bank(ObjectInput input, ArrayNode output) {
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

    public User getUser(String email) {
        if (!users.containsKey(email)) {
            throw new NoSuchElementException("User not found");
        }
        return users.get(email);
    }

    public Account getAccount(String iban) {
        if (!accounts.containsKey(iban)) {
            throw new NoSuchElementException("Account not found");
        }
        return accounts.get(iban);
    }

    public Card getCard(String cardNumber) {
        if (!cards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Card not found");
        }
        return cards.get(cardNumber);
    }

    public void performCommands(CommandInput input) {
        Command command = createCommand(input);
        if (command != null) {
            command.execute();
        }
    }

    private Command createCommand(CommandInput input) {
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
