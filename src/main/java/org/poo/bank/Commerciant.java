package org.poo.bank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
/**
 * Class that represents a commerciant.
 */
public class Commerciant {
    private String name;
    private double moneyReceived;

    public Commerciant(final String name) {
        this.name = name;
        moneyReceived = 0;
    }

    /**
     * Method that receives money from an online payment.
     * @param amount the amount of money received from the payment.
     */
    public void receiveMoney(final double amount) {
        moneyReceived += amount;
    }

}
