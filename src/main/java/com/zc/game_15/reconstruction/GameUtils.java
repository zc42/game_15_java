package com.zc.game_15.reconstruction;

import com.zc.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.zc.utils.Pair.P;

public class GameUtils {

    public static List<Integer> makeMove(List<Integer> state, Action action) {
        int hole = -1;
        int i0 = state.indexOf(hole);
        Pair<Integer, Integer> xy = getXY(i0);
        Integer x = xy.getKey();
        Integer y = xy.getValue();
        if (action == Action.L) x = x - 1;
        if (action == Action.R) x = x + 1;
        if (action == Action.U) y = y - 1;
        if (action == Action.D) y = y + 1;

        int i1 = getIndex(x, y);

//        try {
        state = new ArrayList<>(state);
        Integer v = state.get(i1);
        state.set(i0, v);
        state.set(i1, hole);
        return state;
    }

    public static Pair<Integer, Integer> getXY(int index) {
        int x = index % 4;
        int y = index / 4;
        return P(x, y);
    }

    public static int getIndex(Integer x, Integer y) {
        return y * 4 + x;
    }

    public static List<Action> getValidMoves(int index) {
        Pair<Integer, Integer> xy = getXY(index);
        List<Action> moves = Arrays.stream(Action.values()).collect(Collectors.toList());
        if (xy.getKey() == 0) moves.remove(Action.L);
        if (xy.getKey() == 3) moves.remove(Action.R);
        if (xy.getValue() == 0) moves.remove(Action.U);
        if (xy.getValue() == 3) moves.remove(Action.D);
        return moves;
    }

    public static List<Action> getValidMoves(int i, List<Integer> fixedStateIndexes) {
        List<Action> moves = getValidMoves(i);
        Pair<Integer, Integer> xy = getXY(i);

        if (contains(fixedStateIndexes, xy.getKey() - 1, xy.getValue())) moves.remove(Action.L);
        if (contains(fixedStateIndexes, xy.getKey() + 1, xy.getValue())) moves.remove(Action.R);
        if (contains(fixedStateIndexes, xy.getKey(), xy.getValue() - 1)) moves.remove(Action.U);
        if (contains(fixedStateIndexes, xy.getKey(), xy.getValue() + 1)) moves.remove(Action.D);

        return moves;
    }

    private static boolean contains(List<Integer> fixedStateIndexes, int x, int y) {
        return fixedStateIndexes.contains(getIndex(x, y));
    }


    public static String stateAsString(List<Integer> state, List<Integer> goals) {
        return IntStream.range(0, 16).boxed()
                .map(e -> {
                    String v;
                    Integer o = state.get(e);
                    if (o == -1) v = ConsoleUtils.blue("*");
                    else if (goals.contains(o)) v = ConsoleUtils.color(o + "", 100);
                    else if (goals.contains(e + 1)) v = ConsoleUtils.green(o + "");
                    else v = String.valueOf(o);//" ";
                    v = v + "\t";
                    if (e != 0 && (e + 1) % 4 == 0) {
                        v = v + "\n";
                    }
                    return v;
                })
                .collect(Collectors.joining(""));
    }

    public static Action getReverseAction(Action action) {
        return action == Action.D ? Action.U
                : action == Action.U ? Action.D
                : action == Action.L ? Action.R
                : action == Action.R ? Action.L : null;
    }

}
