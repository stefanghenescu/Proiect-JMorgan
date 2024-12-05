package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Card;
import org.poo.bank.SetupBank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class JsonNode {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static ObjectNode writeUsers(CommandInput command, SetupBank bank) {
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

    private static ObjectNode writeOneUser(User user) {
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

    private static ObjectNode writeAccount(Account account) {
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

    private static ObjectNode writeCard(Card card) {
        ObjectNode cardNode = MAPPER.createObjectNode();

        cardNode.put("cardNumber", card.getNumber());
        cardNode.put("status", card.getStatus());

        return cardNode;
    }
}
