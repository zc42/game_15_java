package com.zc.game_15.reconstruction;

import com.zc.utils.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zc.game_15.reconstruction.GameUtils.*;
import static com.zc.utils.Utils.prnt;
import static com.zc.utils.Utils.prntln;

public class Environment {
    private EnvironmentState state;
    private static StateProducer stateProducer;
    private List<Integer> goals;

    @Getter
    private Action reverseAction = null;
    private List<Action> bigCircleAction1 = new ArrayList<>();
    private List<Action> bigCircleAction2 = new ArrayList<>();
    private List<Action> smallCircleAction1 = new ArrayList<>();
    private List<Action> smallCircleAction2 = new ArrayList<>();
    private List<Action> circleAction = new ArrayList<>();

    public Environment(StateProducer stateProducer) {
        this.stateProducer = stateProducer;
    }

    public static boolean isTerminalSuccess(EnvironmentState state) {
        return isTerminalSuccess(state.getState(), state.getGoals());
    }

    public void reset() {
        reverseAction = null;

        circleAction.clear();
        bigCircleAction1.clear();
        bigCircleAction2.clear();
        smallCircleAction1.clear();
        smallCircleAction2.clear();

        bigCircleAction1.addAll(List.of(Action.L, Action.L, Action.D, Action.D, Action.R, Action.R, Action.U, Action.U));
        bigCircleAction2.addAll(List.of(Action.R, Action.R, Action.D, Action.D, Action.L, Action.L, Action.U, Action.U));
        smallCircleAction1.addAll(List.of(Action.L, Action.D, Action.R, Action.U, Action.L));
        smallCircleAction2.addAll(List.of(Action.R, Action.D, Action.L, Action.U, Action.R));
    }

    public EnvironmentState getInitState() {
        stateProducer.resetState();
        List<Integer> state = stateProducer.getState();
        this.goals = stateProducer.getGoals();
        this.state = new EnvironmentState(state, stateProducer);
        return this.state;
    }

    public static List<Action> getPossibleActions(EnvironmentState state) {
        int io = state.getState().indexOf(-1);
        List<Integer> fixedStateIndexes = state.getFixedElements().stream().map(e -> e - 1).collect(Collectors.toList());
        return getValidMoves(io, fixedStateIndexes);
    }

    public EnvironmentActionResult executeAction(EnvironmentState state0, Action action) {

        List<Integer> newState = makeMove(state0.getState(), action);
        EnvironmentState environmentState = new EnvironmentState(newState, stateProducer);

        boolean isTerminal = isTerminalSuccess(newState, goals);

        state = new EnvironmentState(newState, stateProducer);

        float r = Float.NaN;

        reverseAction = GameUtils.getReverseAction(action);

        circleAction.add(action);
        if (circleAction.size() > 8) circleAction.remove(0);
        if (circleAction.equals(bigCircleAction1)
                || circleAction.equals(bigCircleAction2)
                || circleAction.equals(smallCircleAction1)
                || circleAction.equals(smallCircleAction2)
        ) {
            isTerminal = true;
        }

        int io = state.getState().indexOf(-1);
        if (stateProducer.isLockedIndex(io)) {
            isTerminal = true;
            r = -1;
        }

        if (Float.isNaN(r)) {
            r = getReward(newState, goals);
        }

        return new EnvironmentActionResult(environmentState, action, r, isTerminal);
    }

    public static boolean isTerminalSuccess(List<Integer> newState, List<Integer> goals) {

        if (newState.size() != 16) throw new RuntimeException("newState.size() != 16");

        return goals.stream()
                .map(e -> Objects.equals(newState.get(e - 1), e))
                .filter(e -> e)
                .count() == goals.size();
    }

    public void prntInfo() {
        prnt("\n\n================================================\n");

        List<Integer> state = this.state.getState();
        int io = state.indexOf(-1);
        Pair<Integer, Integer> xy = getXY(io);
        prntln(xy);
        int indx = getIndex(xy.getKey(), xy.getValue());
        prntln(io + " - " + indx);
        List<Action> moves = getValidMoves(io);
        prntln(moves);
        float r = getReward(state, goals);
        prntln(r);
        prnt("\n");

        String stateAsString = stateAsString(state, goals);
        prntln(stateAsString);
    }


    private float getReward(List<Integer> state, List<Integer> goals) {
        int ih = state.indexOf(-1);
        Pair<Integer, Integer> xyh = getXY(ih);

        List<Float> floatStream = goals.stream()
                .map(e -> getDistance(getXY(state.indexOf(e)), getXY(e - 1)))
                .collect(Collectors.toList());
        double d0Sum = floatStream.stream()
                .mapToDouble(e -> e)
                .sum();

        if (d0Sum == 0) {
            return 100.5f;
        }

        double d1Sum = goals.stream().map(e -> getDistance(getXY(state.indexOf(e)), xyh))
                .mapToDouble(e -> e)
                .sum();

        prntln("d0Sum: " + d0Sum);
        prntln("d1Sum: " + d1Sum);

        return (float) (1 / (d0Sum + d1Sum));
    }

    private float getDistance(Pair<Integer, Integer> v1, Pair<Integer, Integer> v2) {
        double pow1 = Math.pow(v2.getKey() - v1.getKey(), 2);
        double pow2 = Math.pow(v2.getValue() - v1.getValue(), 2);
        double v = Math.sqrt(pow1 + pow2);
        return (float) v;
    }
}
