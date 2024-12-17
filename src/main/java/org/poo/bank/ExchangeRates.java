package org.poo.bank;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRates {
    private Map<String, Map<String, Double>> graph = new HashMap<>();

    public void addRate(final String fromCurrency, final String toCurrency, final double rate) {
        if (!graph.containsKey(fromCurrency)) {
            graph.put(fromCurrency, new HashMap<>());
        }
        graph.get(fromCurrency).put(toCurrency, rate);

        if (!graph.containsKey(toCurrency)) {
            graph.put(toCurrency, new HashMap<>());
        }
        graph.get(toCurrency).put(fromCurrency, 1 / rate);
    }

    private void calculateAllRates() {
        for (String k : graph.keySet()) {
            for (String i : graph.keySet()) {
                for (String j : graph.keySet()) {
                    if (graph.get(i).containsKey(k) && graph.get(k).containsKey(j)) {
                        double indirectRate = graph.get(i).get(k) * graph.get(k).get(j);

                        if (!graph.get(i).containsKey(j)) {
                            graph.get(i).put(j, indirectRate);
                            graph.get(j).put(i, 1 / indirectRate);
                        }
                    }
                }
            }
        }
    }

    public double getRate(final String fromCurrency, final String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1;
        }

        if (graph.containsKey(fromCurrency) && graph.get(fromCurrency).containsKey(toCurrency)) {
            return graph.get(fromCurrency).get(toCurrency);
        }

        // If the rate is not directly available, calculate it
        calculateAllRates();
        return graph.get(fromCurrency).get(toCurrency);
    }
}
