package org.poo.bank;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents the exchange rates between different currencies.
 * This implementation uses a graph structure to represent currencies as nodes
 * and their exchange rates as weighted edges between them. The graph enables
 * both direct and indirect rate calculation.
 */
public class ExchangeRates {
    private Map<String, Map<String, Double>> graph = new HashMap<>();

    /**
     * Method that adds a direct exchange rate to the graph. If the inverse rate does not exist, it
     * will be added as well.
     * @param fromCurrency the currency to convert from
     * @param toCurrency the currency to convert to
     * @param rate the exchange rate
     */
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

    /**
     * Fills in all possible indirect exchange rates using the Floyd-Warshall algorithm-like
     * approach. Ensures that all reachable currency pairs have an exchange rate derived from
     * known rates.
     */
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

    /**
     * Method that retrieves the exchange rate between two currencies.If the rate is not directly
     * available, all rates will be calculated to determine an indirect rate.
     * @param fromCurrency the currency to convert from
     * @param toCurrency the currency to convert to
     * @return the exchange rate between the two currencies
     */
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
