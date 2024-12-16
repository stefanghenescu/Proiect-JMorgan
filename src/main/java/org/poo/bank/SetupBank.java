package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.actions.Command;
import org.poo.fileio.*;

import javax.management.monitor.StringMonitor;
import java.util.*;

@Setter
@Getter
public class SetupBank {
    private Map<String, User> users = new LinkedHashMap<>();
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, Card> cards = new HashMap<>();
    private ExchangeRates exchangeRates = new ExchangeRates();
    private Map<String, String> aliases = new HashMap<>();
    private ArrayNode output;

    public SetupBank(ObjectInput input, ArrayNode output) {
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

    public void performCommands(CommandInput input) {
        switch (input.getCommand()) {
    case "printUsers" -> Command.printUsers(this, input, output);
    case "addAccount" -> Command.addAccount(this, input);
    case "addFunds" -> Command.addFunds(this, input);
    case "createCard", "createOneTimeCard" -> Command.createCard(this, input);
    case "deleteAccount" -> Command.deleteAccount(this, input, output);
    case "deleteCard" -> Command.deleteCard(this, input);
    case "setMinBalance" -> Command.setMinBalance(this, input);
    case "payOnline" -> Command.payOnline(this, input, output);
    case "sendMoney" -> Command.sendMoney(this, input);
    case "printTransactions" -> Command.printTransactions(this, input, output);
    case "setAlias" -> Command.setAlias(this, input);
    case "checkCardStatus" -> Command.checkCardStatus(this, input, output);
    case "splitPayment" -> Command.splitPayment(this, input, output);
    case "addInterest" -> Command.addInterest(this, input, output);
    case "changeInterestRate" -> Command.changeInterestRate(this, input, output);
    case "report", "spendingsReport" -> Command.makeReport(this, input, output);
    default -> {
        //throw new IllegalArgumentException("Invalid command");
    }
}
    }
}
