package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.SplitPayment;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.List;
import java.util.Optional;

public final class AcceptSplitPaymentCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public AcceptSplitPaymentCommand(final Bank bank, final CommandInput command,
                                     final ArrayNode output) {
        this.bank = bank;
        this.command = command;
        this.output = output;
    }

    /**
     * Method responsible for accepting a split payment for a specific user.
     * If the user is not found, an error message is added to the output.
     */
    @Override
    public void execute() {
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
        // has the same type as the command
        Optional<SplitPayment> optionalSplitPayment = pendingPayments.stream()
                .filter(payment -> payment.getType().equals(command
                                                                            .getSplitPaymentType()))
                .filter(payment -> !Boolean.TRUE.equals(payment.getUserResponses()
                                                                          .get(command.getEmail())))
                .findFirst();

        // if there is no such payment, return
        if (optionalSplitPayment.isEmpty()) {
            return;
        }

        SplitPayment splitPayment = optionalSplitPayment.get();

        // accept the payment and check if all users have accepted it
        splitPayment.getUserResponses().put(command.getEmail(), true);

        boolean allAccepted = splitPayment.getUserResponses().values().stream()
                                            .allMatch(Boolean.TRUE::equals);

        // if all users have accepted the payment, process it and clear the pending payments
        if (allAccepted) {
            splitPayment.process(bank);
            splitPayment.clearForAllUsers();
        }
    }
}
