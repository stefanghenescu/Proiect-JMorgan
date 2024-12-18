package org.poo.bank.cards;

import org.poo.bank.Bank;
import org.poo.bank.account.Account;
import org.poo.commands.CreateCardCommand;
import org.poo.commands.DeleteCardCommand;
import org.poo.fileio.CommandInput;

public class CardOneTimeUse extends Card {
    private final Bank bank;
    public CardOneTimeUse(final Account ownerAccount, final Bank bank) {
        super(ownerAccount);
        this.bank = bank;
    }

    @Override
    public boolean payOnline(final double amount, final CommandInput command) {
        boolean successPayment = super.payOnline(amount, command);
        if (successPayment) {
            createNewCard(command);
        }
        return successPayment;
    }

    private void createNewCard(final CommandInput command) {
        // After paying with the card, delete it
        DeleteCardCommand deleteCardCommand = new DeleteCardCommand(bank, command);
        deleteCardCommand.execute();

        // Create a new card
        command.setCommand("createOneTimeCard");
        command.setAccount(getOwner().getIban());

        CreateCardCommand createCardCommand = new CreateCardCommand(bank, command);
        createCardCommand.execute();
    }
}
