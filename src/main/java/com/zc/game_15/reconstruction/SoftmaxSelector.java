package com.zc.game_15.reconstruction;

import java.util.*;
import java.util.stream.Collectors;

public class SoftmaxSelector {

    private Random random = new Random();
    private final Map<Integer, List<Double>> probMap = new HashMap<>();

    public Optional<Action> selectActionSoftmax(int key, List<Map.Entry<Action, Double>> entries) {
        if (probMap.containsKey(key)) return getAction(entries, probMap.get(key));
        List<Double> probabilities = getProbabilities(entries);
        probMap.put(key, probabilities);
        return getAction(entries, probabilities);
    }

    private static List<Double> getProbabilities(List<Map.Entry<Action, Double>> entries) {
        // Step 2: Compute softmax probabilities
        double temperature = 1; // you can adjust (lower = greedier)
        List<Double> expValues = new ArrayList<>();
        double sumExp = 0.0;

        for (Map.Entry<Action, Double> entry : entries) {
            double exp = Math.exp(entry.getValue() / temperature);
            expValues.add(exp);
            sumExp += exp;
        }

        // Step 3: Normalize to get probabilities
        double finalSumExp = sumExp;
        return expValues.stream()
                .map(val -> val / finalSumExp)
                .collect(Collectors.toList());
    }

    private Optional<Action> getAction(List<Map.Entry<Action, Double>> entries, List<Double> probs) {
        // Step 4: Randomly select based on probabilities
        double r = random.nextDouble();
        double cumulative = 0.0;
        for (int i = 0; i < entries.size(); i++) {
            cumulative += probs.get(i);
            if (r <= cumulative) {
                return Optional.of(entries.get(i).getKey());
            }
        }

        // Should not happen, but fallback
        return Optional.empty();
    }
}
