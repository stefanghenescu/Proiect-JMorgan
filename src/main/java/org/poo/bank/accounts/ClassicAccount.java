package org.poo.bank.accounts;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

/**
 * Class that represents a classic account and extends the Account class.
 */
public final class ClassicAccount extends Account {
    public ClassicAccount(final CommandInput commandInput) {
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
