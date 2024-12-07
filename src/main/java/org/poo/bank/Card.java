package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.utils.Utils;

@Setter
@Getter
public class Card {
    private String status;
    private String number;
    private Account owner;

    public Card(Account ownerAccount) {
        number = Utils.generateCardNumber();
        status = "active";
        owner = ownerAccount;
    }

    public static Card getCard(SetupBank bank, String number) {
        return bank.getCards().get(number);
    }

    public void payOnline(double amount) {
        owner.withdraw(amount);
    }
}
