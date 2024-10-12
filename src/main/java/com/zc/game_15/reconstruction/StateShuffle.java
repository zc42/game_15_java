package com.zc.game_15.reconstruction;

import java.util.List;
import java.util.stream.Collectors;

import static com.zc.utils.RandomComparator.random;
import static com.zc.utils.Utils.prntln;

public class StateShuffle {

    public static void main(String[] args) {
        StateProducer stateProducer = StateProducer.generateLessons().get(0);
        List<Integer> state = stateProducer.getState();
        List<Integer> goals = stateProducer.getGoals();

        prntState(state, goals);
        state = shuffle(state, stateProducer.getLockedStateElements(), 1000);
        prntState(state, goals);

    }

    public static List<Integer> shuffle(List<Integer> state, List<Integer> lockedStateElements, int steps) {
        List<Integer> fixedStateIndexes = lockedStateElements.stream()
                .map(e -> e - 1)
                .collect(Collectors.toList());

        int i = 0;
        while (i < steps) {
            state = makeRandomMove(state, fixedStateIndexes);
            i++;
        }
        return state;
    }

    private static void prntState(List<Integer> state, List<Integer> goals) {
        prntln(GameUtils.stateAsString(state, goals));
    }

    static List<Integer> makeRandomMove(List<Integer> state, List<Integer> fixedStateIndexes) {
        int i = state.indexOf(-1);
        List<Action> moves = GameUtils.getValidMoves(i, fixedStateIndexes);
        Action action = moves.stream().min(random()).orElseThrow();
        state = GameUtils.makeMove(state, action);
        return state;
    }
}
