package com.zc.game_15.reconstruction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zc.utils.Utils.prnt;
import static com.zc.utils.Utils.prntln;

@Getter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class QTableRow implements Serializable {
    private final EnvironmentState state;
    public final Map<Action, Double> qValues = new HashMap<>();

    public void setValue(Action action, double qValue) {
        List<Action> moves = Environment.getPossibleActions(state);
        if (qValues.isEmpty()) {
            moves.forEach(e -> qValues.put(e, 0d));
        }
        if (!moves.contains(action)) {
            prnt("WARNING: !moves.contains(action)");
            return;
        }
        qValues.put(action, qValue);
    }

    public double getValue(Action action) {
        return qValues.entrySet().stream()
                .filter(e -> e.getKey() == action)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0d);
    }

    public Action getActionWithMaxValue(Action lastAction) {
        List<Action> possibleActions = Environment.getPossibleActions(state);
        possibleActions.remove(lastAction);
        Optional<Action> actionOption = getAction(lastAction);

        if (actionOption.isEmpty()) {
            prntln("WARNING: no action found");
            return !possibleActions.isEmpty() ? possibleActions.get(0) : Action.D;
        } else {
            return actionOption.get();
        }
    }

    public static Action getActionWithMaxValue(EnvironmentState state, Action action, Action forbiddenAction) {
        List<Action> possibleActions = Environment.getPossibleActions(state);
        possibleActions.remove(forbiddenAction);
        Optional<Action> actionOption = action != null && action != forbiddenAction
                ? Optional.of(action)
                : Optional.empty();

        if (actionOption.isEmpty()) {
            prntln("WARNING: no action found");
            return !possibleActions.isEmpty() ? possibleActions.get(0) : Action.D;
        } else {
            return actionOption.get();
        }
    }

    private Optional<Action> getAction(Action lastAction) {
        return qValues.entrySet().stream()
                .filter(e -> e.getKey() != lastAction)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Double getMaxValue() {
        return qValues.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0d);
    }
}
