package org.poo.bank.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

@Getter
@Setter

/**
 * Class that represents a savings account and extends the Account class.
 * This type of account has also an interest rate.
 */
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(final CommandInput commandInput) {
        super(commandInput);
        interestRate = commandInput.getInterestRate();
    }

}
