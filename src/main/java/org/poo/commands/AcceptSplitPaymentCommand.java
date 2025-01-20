package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.SplitPayment;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.JsonOutput;

import java.util.List;
import java.util.Optional;

public class AcceptSplitPaymentCommand implements Command {
    private final Bank bank;
    private final CommandInput command;
    private final ArrayNode output;

    public AcceptSplitPaymentCommand(final Bank bank, final CommandInput command, final ArrayNode output) {
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

        if (user.getEmail().equals("Dominique-Isabelle_Gautier@yahoo.fr")) {
            System.out.println(user.getPendingPayments());
        }

        List<SplitPayment> pendingPayments = user.getPendingPayments();
        if (pendingPayments.isEmpty()) {
            return;
        }

        // select the first split payment that has not been accepted by the user (is not true) and
        // has the same type as the command
        Optional<SplitPayment> optionalSplitPayment = pendingPayments.stream()
                .filter(payment -> payment.getType().equals(command.getSplitPaymentType()))
                .filter(payment -> !Boolean.TRUE.equals(payment.getUserResponses().get(command.getEmail())))
                .findFirst();

        if (optionalSplitPayment.isEmpty()) {
            return;
        }

        SplitPayment splitPayment = optionalSplitPayment.get();

        splitPayment.getUserResponses().put(command.getEmail(), true);

        boolean allAccepted = splitPayment.getUserResponses().values().stream()
                                            .allMatch(Boolean.TRUE::equals);

        if (allAccepted) {
            splitPayment.process(bank);
            splitPayment.clearForAllUsers();
        }
    }
}
