package org.poo.bank;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRates {
    private Map<String, Map<String, Double>> graph = new HashMap<>();

    public void addRate(String fromCurrency, String toCurrency, double rate) {
        if (!graph.containsKey(fromCurrency)) {
            graph.put(fromCurrency, new HashMap<>());
        }
        graph.get(fromCurrency).put(toCurrency, rate);
    }

    public double getRate(String fromCurrency, String toCurrency) {
        return graph.get(fromCurrency).get(toCurrency);
    }
}
