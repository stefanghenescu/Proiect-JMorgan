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
    private final String accountIBAN;
    private final String senderIBAN;
    private final String classicAccountIBAN;
    private final String savingsAccountIBAN;
    private final String receiverIBAN;
    private final String transferType;
    private final String commerciant;
    private final String currency;
    private final List<String> involvedAccounts;
    private final List<Double> amountForUsers;
    private final String splitPaymentType;
    private final String newPlanType;
    private final String error;

    /**
     * JsonIgnore is used to exclude the field from the JSON output
     * Amount and amountString are used to store the amount of the transaction
     * This will be taken care in a method that returns the amount as an Object
     */
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
        accountIBAN = builder.accountIBAN;
        senderIBAN = builder.senderIBAN;
        receiverIBAN = builder.receiverIBAN;
        classicAccountIBAN = builder.classicAccountIBAN;
        savingsAccountIBAN = builder.savingsAccountIBAN;
        amount = builder.amount;
        amountString = builder.amountString;
        transferType = builder.transferType;
        commerciant = builder.commerciant;
        currency = builder.currency;
        involvedAccounts = builder.involvedAccounts;
        amountForUsers = builder.amountForUsers;
        splitPaymentType = builder.splitPaymentType;
        newPlanType = builder.newPlanType;
        error = builder.error;
    }

    /**
     * Method that returns the amount of the transaction.
     * If the amount is a string, it returns the string.
     * If the amount is a Double, it returns the Double. I used Double instead of primitive double
     * because of the comparison with null.
     * JsonProperty is used to change the name of the field in the JSON output.
     * @return the amount of the transaction as an Object
     */
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
        private String accountIBAN;
        private String senderIBAN;
        private String receiverIBAN;
        private String classicAccountIBAN;
        private String savingsAccountIBAN;
        private Double amount;
        private String amountString;
        private String transferType;
        private String commerciant;
        private String currency;
        private List<String> involvedAccounts;
        private List<Double> amountForUsers;
        private String splitPaymentType;
        private String newPlanType;
        private String error;

        public TransactionBuilder(final long timestamp, final String description) {
            this.timestamp = timestamp;
            this.description = description;
        }

        /**
         * Method that sets the card number of the transaction
         * @param cardNumber the card number for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder card(final String cardNumber) {
            card = cardNumber;
            return this;
        }

        /**
         * Method that sets the cardholder of the transaction
         * @param holder the cardholder for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder cardHolder(final String holder) {
            cardHolder = holder;
            return this;
        }

        /**
         * Method that sets the account IBAN of the transaction
         * @param accountIban the account IBAN for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder account(final String accountIban) {
            account = accountIban;
            return this;
        }

        /**
         * Method that sets the account IBAN of the transaction
         * It is different from the account method because it is used in the JSON output
         * and the field name is different
         * @param accountIban the account IBAN for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder accountIBAN(final String accountIban) {
            accountIBAN = accountIban;
            return this;
        }

        /**
         * Method that sets the sender IBAN of the transaction
         * @param senderAccountIBAN the sender account IBAN for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder senderIBAN(final String senderAccountIBAN) {
            senderIBAN = senderAccountIBAN;
            return this;
        }

        /**
         * Method that sets the receiver IBAN of the transaction
         * @param receiverAccountIBAN the receiver account IBAN for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder receiverIBAN(final String receiverAccountIBAN) {
            receiverIBAN = receiverAccountIBAN;
            return this;
        }

        /**
         * Method that sets the classic account IBAN of the transaction
         * @param classicIBAN the classic account IBAN for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder classicAccountIBAN(final String classicIBAN) {
            this.classicAccountIBAN = classicIBAN;
            return this;
        }

        /**
         * Method that sets the savings account IBAN of the transaction
         * @param savingsIBAN the savings account IBAN for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder savingsAccountIBAN(final String savingsIBAN) {
            this.savingsAccountIBAN = savingsIBAN;
            return this;
        }

        /**
         * Method that sets the amount of the transaction as a Double
         * @param transactionAmount the amount of the transaction as a Double
         * @return the current builder instance
         */
        public TransactionBuilder amount(final Double transactionAmount) {
            amount = transactionAmount;
            return this;
        }

        /**
         * Method that sets the amount of the transaction as a String
         * This is used when the currency has to be displayed in the transaction in the amount
         * field
         * @param transactionAmount the amount of the transaction as a String
         * @return the current builder instance
         */
        public TransactionBuilder amountString(final String transactionAmount) {
            amountString = transactionAmount;
            return this;
        }

        /**
         * Method that sets the transfer type of the transaction
         * @param transfer the transfer type of the transaction
         * @return the current builder instance
         */
        public TransactionBuilder transferType(final String transfer) {
            transferType = transfer;
            return this;
        }

        /**
         * Method that sets the commerciant of the transaction
         * @param commerciantName the commerciant name for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder commerciant(final String commerciantName) {
            commerciant = commerciantName;
            return this;
        }

        /**
         * Method that sets the currency of the transaction
         * @param currencyName the currency name for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder currency(final String currencyName) {
            currency = currencyName;
            return this;
        }

        /**
         * Method that sets the accounts involved in the transaction
         * @param accountsInvolved the accounts involved in the transaction
         * @return the current builder instance
         */
        public TransactionBuilder involvedAccounts(final List<String> accountsInvolved) {
            involvedAccounts = accountsInvolved;
            return this;
        }

        /**
         * Method that sets the amount for each user in the transaction when splitting a payment
         * @param amountPerUsers the amount for each user in the transaction
         * @return the current builder instance
         */
        public TransactionBuilder amountForUsers(final List<Double> amountPerUsers) {
            this.amountForUsers = amountPerUsers;
            return this;
        }

        /**
         * Method that sets the split payment type of the transaction
         * @param splitType the split payment type for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder splitPaymentType(final String splitType) {
            splitPaymentType = splitType;
            return this;
        }

        /**
         * Method that sets the new plan type of the transaction
         * @param planType the new plan type for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder newPlanType(final String planType) {
            newPlanType = planType;
            return this;
        }

        /**
         * Method that sets the error message of the transaction
         * @param errorMessage the error message for the transaction
         * @return the current builder instance
         */
        public TransactionBuilder error(final String errorMessage) {
            error = errorMessage;
            return this;
        }

        /**
         * Method that builds the final transaction object
         * @return a new transaction object
         */
        public Transaction build() {
            return new Transaction(this);
        }
    }
}
