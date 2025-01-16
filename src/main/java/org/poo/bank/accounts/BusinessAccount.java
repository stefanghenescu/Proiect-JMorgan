package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

public final class BusinessAccount extends Account{
    public BusinessAccount(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void addInterestRate(final CommandInput command, final ArrayNode output) {
        output.add(JsonOutput.writeErrorSavingAccount(command));
    }

    @Override
    public void changeInterestRate(final CommandInput command, final ArrayNode output) {
        output.add(JsonOutput.writeErrorSavingAccount(command));
    }

}
