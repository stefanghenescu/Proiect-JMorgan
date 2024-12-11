package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
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

    private Transaction(TransactionBuilder builder) {
        this.timestamp = builder.timestamp;
        this.description = builder.description;
        this.card = builder.card;
        this.cardHolder = builder.cardHolder;
        this.account = builder.account;
        this.senderIBAN = builder.senderIBAN;
        this.receiverIBAN = builder.receiverIBAN;
        this.amount = builder.amount;
        this.amountString = builder.amountString;
        this.transferType = builder.transferType;
        this.commerciant = builder.commerciant;
        this.currency = builder.currency;
        this.involvedAccounts = builder.involvedAccounts;
        this.error = builder.error;
    }

    @JsonProperty("amount")
    public Object getDynamicAmount() {
        if (amountString != null) {
            return amountString;
        }
        if (amount != null) {
            return amount;
        }
        return null;
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

        public TransactionBuilder(long timestamp, String description) {
            this.timestamp = timestamp;
            this.description = description;
        }

        public TransactionBuilder card(String card) {
            this.card = card;
            return this;
        }

        public TransactionBuilder cardHolder(String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public TransactionBuilder account(String account) {
            this.account = account;
            return this;
        }

        public TransactionBuilder senderIBAN(String senderIBAN) {
            this.senderIBAN = senderIBAN;
            return this;
        }

        public TransactionBuilder receiverIBAN(String receiverIBAN) {
            this.receiverIBAN = receiverIBAN;
            return this;
        }

        public TransactionBuilder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder amountString(String amountString) {
            this.amountString = amountString;
            return this;
        }

        public TransactionBuilder transferType(String transferType) {
            this.transferType = transferType;
            return this;
        }

        public TransactionBuilder commerciant(String commerciant) {
            this.commerciant = commerciant;
            return this;
        }

        public TransactionBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public TransactionBuilder involvedAccounts(List<String> involvedAccounts) {
            this.involvedAccounts = involvedAccounts;
            return this;
        }

        public TransactionBuilder error(String error) {
            this.error = error;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
