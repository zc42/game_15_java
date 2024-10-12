package com.zc.game_15.reconstruction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Setter
@Getter
public class EnvironmentState implements Serializable {

    private final List<Integer> state;
    private final List<Integer> goals;
    public final List<Integer> fixedElements;

    public EnvironmentState(List<Integer> state, StateProducer stateProducer) {
        this.state = new ArrayList<>(state);
        this.goals = new ArrayList<>(stateProducer.getGoals());
        this.fixedElements = new ArrayList<>(stateProducer.getLockedStateElements());
    }

    public EnvironmentState(EnvironmentState state) {
        this.state = new ArrayList<>(state.getState());
        this.goals = new ArrayList<>(state.getGoals());
        this.fixedElements = new ArrayList<>(state.getFixedElements());
    }

    public int getHashCodeV2() {
        String ab = IntStream.range(0, 16).boxed()
                .map(e -> {
                    String v;
                    Integer o = state.get(e);
                    if (o == -1) v = "*";
                    else if (goals.contains(o)) v = String.valueOf(o);
                    else if (goals.contains(e + 1)) v = "o";
                    else v = " ";
                    v = v + "\t";
                    if (e != 0 && (e + 1) % 4 == 0) v = v + "\n";
                    return v;
                })
                .collect(Collectors.joining(""));

        return ab.hashCode();
    }

    public String getHashCodeV3__() {
        return IntStream.range(0, 16).boxed()
                .map(e -> {
                    String v;
                    Integer o = state.get(e);
                    if (o == -1) v = "*";
                    else if (goals.contains(o)) v = String.valueOf(o);
                    else if (goals.contains(e + 1)) v = "o";
                    else v = " ";
                    v = v + "\t";
                    if (e != 0 && (e + 1) % 4 == 0) v = v + "\n";
                    return v;


//                    String v;
//                    Integer o = state.get(e);
//                    if (o == -1) v = "*";
//                    else if (goals.contains(o)) v = String.valueOf(o);
//                    else if (goals.contains(e + 1)) v = "o";
//                    else v = " ";
//                    v = v + "_";
//                    if (e != 0 && (e + 1) % 4 == 0) v = v + "|";
//                    return v;
                })
                .collect(Collectors.joining(""));
    }


    public int getHashCode() {
        String a = state.stream().map(String::valueOf).collect(Collectors.joining(","));
        return a.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvironmentState that = (EnvironmentState) o;
        return getHashCode() == that.getHashCode();
    }

    @Override
    public int hashCode() {
        return getHashCode();
    }
}

