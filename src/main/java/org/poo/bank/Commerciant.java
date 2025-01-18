package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

/**
 * Class that represents a commerciant.
 */
@Getter
@Setter
public class Commerciant {
    private String commerciant;
    private int id;
    private String account;
    private String type;
    private String cashbackStrategy;
    private double moneyReceived = 0;

    public Commerciant(final CommerciantInput commerciantInput) {
        commerciant = commerciantInput.getCommerciant();
        id = commerciantInput.getId();
        account = commerciantInput.getAccount();
        type = commerciantInput.getType();
        cashbackStrategy = commerciantInput.getCashbackStrategy();
    }

    /**
     * Method that receives money from an online payment.
     * @param amount the amount of money received from the payment.
     */
    public void receiveMoney(final double amount) {
        moneyReceived += amount;
    }

}
