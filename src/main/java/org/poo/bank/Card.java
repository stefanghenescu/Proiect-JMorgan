package org.poo.bank;

import org.poo.utils.Utils;

public class Card {
    private String status;
    private String number;

    public Card() {
        number = Utils.generateCardNumber();
        status = "active";
    }


}
