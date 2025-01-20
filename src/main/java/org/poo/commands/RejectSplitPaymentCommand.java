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

    public RejectSplitPaymentCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    @Override
    public void execute() {
        User user;

        try {
            user = bank.getUser(command.getEmail());
        } catch (Exception e) {
            output.add(JsonOutput.userNotFound(command));
            return;
        }

        List<SplitPayment> pendingPayments = user.getPendingPayments();
        if (pendingPayments.isEmpty()) {
            return;
        }

        Optional<SplitPayment> optionalSplitPayment = pendingPayments.stream()
                .filter(payment -> payment.getType()
                        .equals(command.getSplitPaymentType()))
                .findFirst();

        if (optionalSplitPayment.isEmpty()) {
            return;
        }

        SplitPayment splitPayment = optionalSplitPayment.get();

        splitPayment.getUserResponses().put(command.getEmail(), false);

        Transaction transaction;

        if (splitPayment.getType().equals("equal")) {
            transaction = new Transaction.TransactionBuilder(splitPayment.getTimestamp(),
                    String.format(SPLIT_PAYMENT_MESSAGE, splitPayment.getAmount(), splitPayment.getCurrency()))
                    .currency(splitPayment.getCurrency())
                    .error("One user rejected the payment.")
                    .amount(splitPayment.getAmount())
                    .splitPaymentType(splitPayment.getType())
                    .build();
        } else {
            List<String> involvedAccounts = splitPayment.getAccounts().stream()
                    .map(Account::getIban)
                    .toList();

            transaction = new Transaction.TransactionBuilder(splitPayment.getTimestamp(),
                    String.format(SPLIT_PAYMENT_MESSAGE, splitPayment.getAmount(), splitPayment.getCurrency()))
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
        splitPayment.clearForAllUsers();
    }
}
