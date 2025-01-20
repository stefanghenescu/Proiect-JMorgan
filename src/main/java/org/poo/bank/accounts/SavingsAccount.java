package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

/**
 * Class that represents a savings account and extends the Account class.
 * This type of account has also an interest rate.
 */
@Getter
@Setter
public final class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(final CommandInput commandInput) {
        super(commandInput);
        interestRate = commandInput.getInterestRate();
    }

    @Override
    public void addInterestRate(final CommandInput command, final ArrayNode output) {
        double interest = getBalance() * getInterestRate();

        addFunds(interest);

        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                "Interest rate income")
                .amount(interest)
                .currency(this.getCurrency())
                .build();

        getOwner().addTransaction(transaction);
        addTransaction(transaction);
    }

    @Override
    public void changeInterestRate(final CommandInput command, final ArrayNode output) {
        setInterestRate(command.getInterestRate());

        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                "Interest rate of the account changed to " + command.getInterestRate())
                .build();

        getOwner().addTransaction(transaction);
        addTransaction(transaction);
    }
}
