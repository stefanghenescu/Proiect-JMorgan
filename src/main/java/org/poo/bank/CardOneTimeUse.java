package org.poo.bank;

public class CardOneTimeUse extends Card {
    public CardOneTimeUse(Account ownerAccount) {
        super(ownerAccount);
    }

    @Override
    public void payOnline(double amount) {
        getOwner().withdraw(amount);

        // delete the card from the account
        getOwner().deleteCard(this);
    }
}
