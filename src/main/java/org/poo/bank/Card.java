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
}
