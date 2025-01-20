package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.SplitPayment;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.JsonOutput;

import java.util.List;
import java.util.Optional;

public class RejectSplitPaymentCommand implements Command {
    private static final String SPLIT_PAYMENT_MESSAGE = "Split payment of %.2f %s";
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public RejectSplitPaymentCommand(final Bank bank, final CommandInput command,
                                     final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for rejecting a split payment for a specific user.
     */
    @Override
    public void execute() {
        // get the user if it exists
        User user;
        try {
            user = bank.getUser(command.getEmail());
        } catch (Exception e) {
            output.add(JsonOutput.userNotFound(command));
            return;
        }

        // get the pending payments of the user
        List<SplitPayment> pendingPayments = user.getPendingPayments();
        if (pendingPayments.isEmpty()) {
            return;
        }

        // select the first split payment that has not been accepted by the user (is not true) and
        // has the same type as the command says
        Optional<SplitPayment> optionalSplitPayment = pendingPayments.stream()
                .filter(payment -> payment.getType()
                        .equals(command.getSplitPaymentType()))
                .filter(payment -> !Boolean.TRUE.equals(payment.getUserResponses()
                                                                        .get(command.getEmail())))
                .findFirst();

        if (optionalSplitPayment.isEmpty()) {
            return;
        }

        SplitPayment splitPayment = optionalSplitPayment.get();

        splitPayment.getUserResponses().put(command.getEmail(), false);

        Transaction transaction;

        // create the transaction for the split payment
        // depending on the type of the split payment (equal or custom) the transactions fields
        // are set differently
        String splitMessage = String.format(SPLIT_PAYMENT_MESSAGE, splitPayment.getAmount(),
                splitPayment.getCurrency());

        if (splitPayment.getType().equals("equal")) {
            transaction = new Transaction.TransactionBuilder(splitPayment.getTimestamp(),
                    splitMessage)
                    .currency(splitPayment.getCurrency())
                    .error("One user rejected the payment.")
                    .amount(splitPayment.getAmount())
                    .splitPaymentType(splitPayment.getType())
                    .build();
        } else {
            // get the involved accounts in the split payment as Strings
            List<String> involvedAccounts = splitPayment.getAccounts().stream()
                    .map(Account::getIban)
                    .toList();

            transaction = new Transaction.TransactionBuilder(splitPayment.getTimestamp(),
                    splitMessage)
                    .currency(splitPayment.getCurrency())
                    .error("One user rejected the payment.")
                    .amountForUsers(splitPayment.getAmountForUsers())
                    .splitPaymentType(splitPayment.getType())
                    .involvedAccounts(involvedAccounts)
                    .build();
        }

        for (Account account : splitPayment.getAccounts()) {
            account.addTransaction(transaction);
            account.getOwner().addTransaction(transaction);
        }

        // remove the split payment from the user's pending payments
        splitPayment.clearForAllUsers();
    }
}
