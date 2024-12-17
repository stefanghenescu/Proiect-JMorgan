package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Transaction {
    private final long timestamp;
    private final String description;
    private final String card;
    private final String cardHolder;
    private final String account;
    private final String senderIBAN;
    private final String receiverIBAN;
    private final String transferType;
    private final String commerciant;
    private final String currency;
    private final List<String> involvedAccounts;
    private final String error;

    @JsonIgnore
    private final Double amount;
    @JsonIgnore
    private final String amountString;

    private Transaction(final TransactionBuilder builder) {
        timestamp = builder.timestamp;
        description = builder.description;
        card = builder.card;
        cardHolder = builder.cardHolder;
        account = builder.account;
        senderIBAN = builder.senderIBAN;
        receiverIBAN = builder.receiverIBAN;
        amount = builder.amount;
        amountString = builder.amountString;
        transferType = builder.transferType;
        commerciant = builder.commerciant;
        currency = builder.currency;
        involvedAccounts = builder.involvedAccounts;
        error = builder.error;
    }

    @JsonProperty("amount")
    public Object getDynamicAmount() {
        if (amountString != null) {
            return amountString;
        }
        return amount;
    }

    public static class TransactionBuilder {
        private final long timestamp;
        private final String description;
        private String card;
        private String cardHolder;
        private String account;
        private String senderIBAN;
        private String receiverIBAN;
        private Double amount;
        private String amountString;
        private String transferType;
        private String commerciant;
        private String currency;
        private List<String> involvedAccounts;
        private String error;

        public TransactionBuilder(final long timestamp, final String description) {
            this.timestamp = timestamp;
            this.description = description;
        }

        public TransactionBuilder card(final String cardNumber) {
            card = cardNumber;
            return this;
        }

        public TransactionBuilder cardHolder(final String holder) {
            cardHolder = holder;
            return this;
        }

        public TransactionBuilder account(final String accountIBAN) {
            account = accountIBAN;
            return this;
        }

        public TransactionBuilder senderIBAN(final String senderAccountIBAN) {
            senderIBAN = senderAccountIBAN;
            return this;
        }

        public TransactionBuilder receiverIBAN(final String receiverAccountIBAN) {
            receiverIBAN = receiverAccountIBAN;
            return this;
        }

        public TransactionBuilder amount(final Double transactionAmount) {
            amount = transactionAmount;
            return this;
        }

        public TransactionBuilder amountString(final String transactionAmount) {
            amountString = transactionAmount;
            return this;
        }

        public TransactionBuilder transferType(final String transfer) {
            transferType = transfer;
            return this;
        }

        public TransactionBuilder commerciant(final String commerciantName) {
            commerciant = commerciantName;
            return this;
        }

        public TransactionBuilder currency(final String currencyName) {
            currency = currencyName;
            return this;
        }

        public TransactionBuilder involvedAccounts(final List<String> accountsInvolved) {
            involvedAccounts = accountsInvolved;
            return this;
        }

        public TransactionBuilder error(final String errorMessage) {
            error = errorMessage;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
