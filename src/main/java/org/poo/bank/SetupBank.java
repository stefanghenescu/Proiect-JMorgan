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
            case "printUsers":
                Command.printUsers(this, input, output);
                break;
            case "addAccount":
                Command.addAccount(this, input);
                break;
            case "addFunds":
                Command.addFunds(this, input);
                break;
            case "createCard", "createOneTimeCard":
                Command.createCard(this, input);
                break;
            case "deleteAccount":
                Command.deleteAccount(this, input, output);
                break;
            case "deleteCard":
                Command.deleteCard(this, input);
                break;
            case "setMinBalance":
                Command.setMinBalance(this, input);
                break;
            case "payOnline":
                Command.payOnline(this, input, output);
                break;
            case "sendMoney":
                Command.sendMoney(this, input);
                break;
            case "printTransactions":
                Command.printTransactions(this, input, output);
                break;
            case "setAlias":
                Command.setAlias(this, input);
                break;
            case "checkCardStatus":
                Command.checkCardStatus(this, input, output);
                break;
            case "splitPayment":
                Command.splitPayment(this, input, output);
                break;
            case "addInterest":
                Command.addInterest(this, input, output);
                break;
            case "changeInterestRate":
                Command.changeInterestRate(this, input, output);
                break;
            case "report", "spendingsReport":
                Command.makeReport(this, input, output);
                break;
            default:
                //throw new IllegalArgumentException("Invalid command");
        }
    }
}
