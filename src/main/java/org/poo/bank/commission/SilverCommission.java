package org.poo.bank.commission;
import org.poo.bank.Bank;

public class SilverCommission implements CommissionStrategy {
    @Override
    public double calculateCommission(final double amount, final Bank bank, final String currency) {
        double ronExchange = bank.getExchangeRates().getRate(currency, "RON");
        if (amount * ronExchange < 500) {
            return amount * 0.001;
        }
        return 0;
    }
}
