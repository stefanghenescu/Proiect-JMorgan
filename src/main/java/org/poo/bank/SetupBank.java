package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.actions.Command;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Setter
@Getter
public class SetupBank {
    private Map<String, User> users = new LinkedHashMap<>();
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, Card> cards = new HashMap<>();
    private ExchangeRates exchangeRates = new ExchangeRates();
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
            case "createCard":
                Command.createCard(this, input);
                break;
            case "deleteAccount":
                Command.deleteAccount(this, input);
                break;
            case "deleteCard":
                Command.deleteCard(this, input);
                break;
            default:
                //throw new IllegalArgumentException("Invalid command");
        }
    }
}
