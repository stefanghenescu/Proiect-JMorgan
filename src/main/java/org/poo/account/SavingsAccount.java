package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(CommandInput commandInput) {
        super(commandInput);
        interestRate = commandInput.getInterestRate();
    }

}
