package org.poo.bank.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(final CommandInput commandInput) {
        super(commandInput);
        interestRate = commandInput.getInterestRate();
    }

}
