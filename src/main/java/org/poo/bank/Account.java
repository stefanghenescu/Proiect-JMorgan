package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Setter
@Getter
public abstract class Account {
    public static final double DEFAULT_BALANCE = 0.0;
    private String iban;
    private double balance;
    private String currency;
    private String accountType;
    private ArrayList<Card> cards = new ArrayList<>();

    public Account(CommandInput commandInput) {
        iban = Utils.generateIBAN();
        balance = DEFAULT_BALANCE;
        currency = commandInput.getCurrency();
        accountType = commandInput.getAccountType();
    }
}

class ClassicAccount extends Account {
    public ClassicAccount(CommandInput commandInput) {
        super(commandInput);
    }
}

@Getter
class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(CommandInput commandInput) {
        super(commandInput);
        interestRate = commandInput.getInterestRate();
    }

}

