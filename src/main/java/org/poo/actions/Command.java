package org.poo.actions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;
import org.poo.account.AccountFactory;
import org.poo.account.SavingsAccount;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;
import org.poo.reports.ReportFactory;
import org.poo.reports.ReportStrategy;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import javax.swing.*;

public class Command {
    public static void printUsers (SetupBank bank, CommandInput command, ArrayNode output) {
        ObjectNode usersArray = JsonOutput.writeUsers(command, bank);
        output.add(usersArray);
    }

    public static void addAccount(SetupBank bank, CommandInput command) {
        // create account
        Account account = AccountFactory.createAccount(command);

        // get email of user to add account
        String userEmail = command.getEmail();

        // get the user with the email from the command
        // add the account to that user
        User userToAddAccount = User.getUser(bank, userEmail);
        if (userToAddAccount == null)
            return;

        account.setOwner(userToAddAccount);
        userToAddAccount.addAccount(account);

        // add the account to the bank database
        bank.getAccounts().put(account.getIban(), account);

        // add the account to the aliases database
        bank.getAliases().put(account.getIban(), account.getIban());

        // transaction for later update()
        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                                                                "New account created")
                .build();
        userToAddAccount.addTransaction(transaction);
        account.addTransaction(transaction);
    }

    public static void addFunds(SetupBank bank, CommandInput command) {
        // get the account to add funds to
        Account account = Account.getAccount(bank, command.getAccount());

        if (account == null) {
            return;
        }

        // add the funds to the account
        account.addFunds(command.getAmount());
    }

    public static void createCard(SetupBank bank, CommandInput command) {
        // get the account to add the card to
        Account account = Account.getAccount(bank, command.getAccount());

        User user = User.getUser(bank, command.getEmail());
        if (user == null)
            return;

        if (!user.getAccounts().contains(account)) {
            // update() transactions with an error message
            return;
            //throw new IllegalArgumentException("User does not own the account");
        }

        Card card;
        if (command.getCommand().equals("createOneTimeCard")) {
            card = new CardOneTimeUse(account);
        } else {
            card = new Card(account);
        }

        // add the card to the account
        account.addCard(card);

        // add the card to the bank database
        bank.getCards().put(card.getNumber(), card);

        // transaction for later update()
        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                                                                "New card created")
                .card(card.getNumber())
                .cardHolder(user.getEmail())
                .account(account.getIban())
                .build();

        user.addTransaction(transaction);
        account.addTransaction(transaction);
    }

    public static void deleteAccount(SetupBank bank, CommandInput command, ArrayNode output) {
        String accountIBAN = bank.getAliases().get(command.getAccount());

        // get the account to delete
        Account account = Account.getAccount(bank, accountIBAN);

        if (account == null) {
            return;
        }

        // get the user to delete the account from
        User user = User.getUser(bank, command.getEmail());

        if (user == null) {
            return;
        }

        // delete the account from the user
        if (user.deleteAccount(account)) {
            // delete the account from the bank database
            bank.getAccounts().remove(account.getIban());
        } else {
            // update transactions with an error message
            Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Account couldn't be deleted - there are funds remaining")
                    .build();

            user.addTransaction(transaction);
        }

        output.add(JsonOutput.eraseAccount(command, account));
    }

    public static void deleteCard(SetupBank bank, CommandInput command) {
        // get the card to delete
        Card card = Card.getCard(bank, command.getCardNumber());

        if (card == null) {
            return;
        }

        // delete the card from the account
        Account ownerAccount = card.getOwner();
        ownerAccount.deleteCard(card);

        // delete the card from the bank database
        bank.getCards().remove(card.getNumber());

        // transaction for later update()
        Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                                                                "The card has been destroyed")
                .card(card.getNumber())
                .cardHolder(ownerAccount.getOwner().getEmail())
                .account(ownerAccount.getIban())
                .build();

        ownerAccount.getOwner().addTransaction(transaction);
        ownerAccount.addTransaction(transaction);
    }

    public static void setMinBalance(SetupBank bank, CommandInput command) {
        String accountIBAN = bank.getAliases().get(command.getAccount());

        // get the account to set the minimum balance
        Account account = Account.getAccount(bank, accountIBAN);

        if (account == null) {
            return;
        }

        // set the minimum balance for the account
        account.setMinBalance(command.getAmount());
    }

    public static void payOnline(SetupBank bank, CommandInput command, ArrayNode output) {
        Card card = Card.getCard(bank, command.getCardNumber());
        User cardOwnerUser = User.getUser(bank, command.getEmail());

        if (card == null) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        Account cardAccount = card.getOwner();
        if (cardOwnerUser == null || !cardOwnerUser.getAccounts().contains(cardAccount)) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        // convert in account currency
        double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(),
                cardAccount.getCurrency());
        double amount = command.getAmount() * exchangeRate;

        boolean paidWithOneTimeCard = card.payOnline(amount, command.getTimestamp(),
                command.getCommerciant());

        if (paidWithOneTimeCard) {
            // delete the card
            Command.deleteCard(bank, command);

            // create a new one-time card
            command.setCommand("createOneTimeCard");
            command.setAccount(cardAccount.getIban());
            Command.createCard(bank, command);
        }
    }

    public static void sendMoney(SetupBank bank, CommandInput command) {
        // get the account to send money from
        String receiverAccountIBAN = bank.getAliases().get(command.getReceiver());

        Account senderAccount = Account.getAccount(bank, command.getAccount());
        Account receiverAccount = Account.getAccount(bank, receiverAccountIBAN);

        if (senderAccount == null || receiverAccount == null) {
            return;
        }

        // send the money
        double amountWithdrawn = senderAccount.withdraw(command.getAmount());

        // convert in receiver account currency
        double exchangeRate = bank.getExchangeRates().getRate(senderAccount.getCurrency(),
                receiverAccount.getCurrency());
        double amount = amountWithdrawn * exchangeRate;

        receiverAccount.addFunds(amount);

        Transaction transactionSender;

        if (amountWithdrawn == 0) {
            // add transaction with an error message
            transactionSender = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Insufficient funds")
                    .build();
        } else {
            transactionSender = new Transaction.TransactionBuilder(command.getTimestamp(),
                    command.getDescription())
                    .senderIBAN(senderAccount.getIban())
                    .receiverIBAN(receiverAccount.getIban())
                    .amountString(amountWithdrawn + " " + senderAccount.getCurrency())
                    .transferType("sent")
                    .build();

            Transaction transactionReceiver =
                    new Transaction.TransactionBuilder(command.getTimestamp(),
                    command.getDescription())
                    .senderIBAN(senderAccount.getIban())
                    .receiverIBAN(receiverAccount.getIban())
                    .amountString(amount + " " + receiverAccount.getCurrency())
                    .transferType("received")
                    .build();

            receiverAccount.getOwner().addTransaction(transactionReceiver);
            receiverAccount.addTransaction(transactionReceiver);
        }
        senderAccount.getOwner().addTransaction(transactionSender);
        senderAccount.addTransaction(transactionSender);
    }

    public static void printTransactions(SetupBank bank, CommandInput command, ArrayNode output) {
        User transactionsUser = User.getUser(bank, command.getEmail());
        ObjectNode transactionsArray = JsonOutput.writeTransactions(command, bank, transactionsUser);
        output.add(transactionsArray);
    }

    public static void setAlias(SetupBank bank, CommandInput command) {
        bank.getAliases().put(command.getAlias(), command.getAccount());
    }

    public static void checkCardStatus(SetupBank bank, CommandInput command, ArrayNode output) {
        Card card = Card.getCard(bank, command.getCardNumber());

        if (card == null) {
            output.add(JsonOutput.cardNotFound(command));
            return;
        }

        card.check(command.getTimestamp());
    }

    public static void splitPayment(SetupBank bank, CommandInput command, ArrayNode output) {
        double amountPerPerson = command.getAmount() / command.getAccounts().size();
        boolean everyonePaid = true;
        Transaction transaction;
        String error = null;

        for (String accountIBAN : command.getAccounts().reversed()) {
            Account account = Account.getAccount(bank, accountIBAN);

            if (account == null) {
                return;
            }

            double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(), account.getCurrency());
            everyonePaid = account.checkEnoughMoney(amountPerPerson * exchangeRate);

            if (!everyonePaid) {
                error = "Account " + accountIBAN + " has insufficient funds for a split payment.";
                break;
            }
        }

        if (everyonePaid) {
            for (String accountIBAN : command.getAccounts()) {
                Account account = Account.getAccount(bank, accountIBAN);
                double exchangeRate = bank.getExchangeRates().getRate(command.getCurrency(), account.getCurrency());
                account.withdraw(amountPerPerson * exchangeRate);
            }
        }

        // add transactions for each account
        transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                // i do like this as ref has amount with 2 decimals evan if it is an int
                // (1269.00 EUR)
                String.format("Split payment of %.2f %s", command.getAmount(), command.getCurrency()))
                .error(error)
                .currency(command.getCurrency())
                .amount(amountPerPerson)
                .involvedAccounts(command.getAccounts())
                .build();

        for (String accountIBAN : command.getAccounts()) {
            Account account = Account.getAccount(bank, accountIBAN);
            account.getOwner().addTransaction(transaction);
            account.addTransaction(transaction);
        }
    }

    public static void addInterest(SetupBank bank, CommandInput command, ArrayNode output) {
        Account account = bank.getAccounts().get(command.getAccount());
        if (!account.getAccountType().equals("savings")) {
            output.add(JsonOutput.writeErrorSavingAccount(command));
            return;
        }

        double interestRate = ((SavingsAccount) account).getInterestRate();
        double interest = account.getBalance() * interestRate;

        account.addFunds(interest);
    }

    public static void changeInterestRate(SetupBank bank, CommandInput command, ArrayNode output) {
        Account account = bank.getAccounts().get(command.getAccount());
        if (!account.getAccountType().equals("savings")) {
            output.add(JsonOutput.writeErrorSavingAccount(command));
            return;
        }

        ((SavingsAccount) account).setInterestRate(command.getInterestRate());

            Transaction transaction = new Transaction.TransactionBuilder(command.getTimestamp(),
                    "Interest rate changed")
                    .account(account.getIban())
                    .build();

        account.getOwner().addTransaction(transaction);
        account.addTransaction(transaction);
    }

    public static void makeReport(SetupBank bank, CommandInput command, ArrayNode output) {
        ReportStrategy reportStrategy = ReportFactory.getReportType(command.getCommand());
        reportStrategy.generateReport(bank, command);

        ObjectNode report = reportStrategy.generateReport(bank, command);
        output.add(report);
    }
}

