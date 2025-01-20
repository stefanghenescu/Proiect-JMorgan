package org.poo.bank.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

/**
 * Class that represents a commerciant.
 */
@Getter
@Setter
public class Commerciant {
    private String commerciant;
    private int id;
    private String account;
    private String type;
    private CashbackStrategy cashbackStrategy;
    private double moneyReceived = 0;

    public Commerciant(final CommerciantInput commerciantInput) {
        commerciant = commerciantInput.getCommerciant();
        id = commerciantInput.getId();
        account = commerciantInput.getAccount();
        type = commerciantInput.getType();
        cashbackStrategy = getCashbackStrategy(commerciantInput.getCashbackStrategy());
    }

    /**
     * Method that receives money from an online payment.
     * @param amount the amount of money received from the payment.
     */
    public void receiveMoney(final double amount) {
        moneyReceived += amount;
    }

    /**
     * Returns the appropriate strategy implementation based on the
     * specified type of cashback and how it's calculated.
     * It's like a factory method for the cashback strategies.
     * @param cashbackType It specifies the type of cashback strategy to be used. Supported values:
     *                     <ul>
     *                         <li>{@code "spendingThreshold"}: Cashback based on reaching a certain
     *                         spending threshold.</li>
     *                         <li>{@code "nrOfTransactions"}: Cashback based on the number of
     *                         transactions made.</li>
     *                     </ul>
     * @return The appropriate cashback strategy implementation.
     */
    public CashbackStrategy getCashbackStrategy(final String cashbackType) {
        return switch (cashbackType) {
            case "spendingThreshold" -> new SpendingThresholdCashback();
            case "nrOfTransactions" -> new NrOfTransactionsCashbak();
            default -> null;
        };
    }

}
