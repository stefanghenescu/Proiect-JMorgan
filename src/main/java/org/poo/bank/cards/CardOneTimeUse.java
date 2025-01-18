package org.poo.bank.cards;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.commands.CreateCardCommand;
import org.poo.commands.DeleteCardCommand;
import org.poo.fileio.CommandInput;

/**
 * Class that represents a one-time use card. This class extends the Card class.
 */
public class CardOneTimeUse extends Card {
    private final Bank bank;
    public CardOneTimeUse(final Account ownerAccount, final Bank bank) {
        super(ownerAccount);
        this.bank = bank;
    }

    /**
     * Method that pays online with the card. It calls the super method and if the payment
     * is successful, it creates a new card.
     * @param amount the amount of money to be paid
     * @param command the command that contains the information about the payment
     * @return true if the payment was successful, false otherwise
     */
    @Override
    public boolean payOnline(final double amount, final CommandInput command) {
        // Pay online with the card
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

        // Create a new card. The command name is "createOneTimeCard" in order to create a
        // one-time use card. Except this, the command is the same as the one that was used to pay
        // online
        command.setCommand("createOneTimeCard");
        command.setAccount(getOwner().getIban());

        CreateCardCommand createCardCommand = new CreateCardCommand(bank, command);
        createCardCommand.execute();
    }
}
