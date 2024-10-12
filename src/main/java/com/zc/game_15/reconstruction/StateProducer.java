package com.zc.game_15.reconstruction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.zc.utils.RandomComparator.random;

@Setter
@Getter
public class StateProducer implements Serializable {

    private List<Integer> goals;
    private List<Integer> lockedStateElements;
    private List<Integer> state;
    private int episodesToTrain;
    private final int lessonNb;

    public final static List<Integer> stateDone = List.of(1, 2, 3, 4,
            5, 6, 7, 8,
            9, 10, 11, 12,
            13, 14, 15, -1);


    private StateProducer(int lessonNb) {
        this.lessonNb = lessonNb;
    }

    public static List<StateProducer> generateLessons0() {
        return List.of
                (
                        state1(0),
                        moveHole(stateX(2, 1), List.of(1, 4)),
                        moveHole(stateX(3, 2), List.of(2, 5)),
                        moveHole(state3_4(3), List.of(3, 6)),
                        moveHole(stateX(5, 4), List.of(6, 7)),
                        moveHole(stateX(6, 5), List.of(5, 8)),
                        moveHole(stateX(7, 6), List.of(6, 9)),
                        moveHole(state7_8(7), List.of(8, 11)),
                        moveHole(state9_13(8), List.of(10, 11)),
                        moveHole(state10_15(9), List.of(9, 13)),
                        state12(10)
                );
    }

    public static List<StateProducer> generateLessonsV1() {
        return List.of
                (
                        state1_2(0),
                        moveHole(state3_4(1), List.of(2, 3, 4)),
                        moveHole(stateX(5, 2), List.of(6, 7)),
                        moveHole(stateX(6, 3), List.of(5, 8)),
                        moveHole(stateX(7, 4), List.of(6, 9)),
                        moveHole(state7_8(5), List.of(8, 11)),
                        moveHole(state9_13(6), List.of(10, 11)),
                        moveHole(state10_15(7), List.of(9, 13)),
                        state12(8)
                );
    }

    public static List<StateProducer> generateLessons() {
        return List.of
                (
                        state1_2(0),
                        moveHole(state3_4(1), List.of(2, 3, 4)),
                        moveHole(state5_6(2), List.of(6, 7)),
                        moveHole(state7_8(3), List.of(8, 9, 6)),
                        moveHole(state9_13(4), List.of(10, 11)),
                        moveHole(state10_15(5), List.of(9, 13)),
                        state12(6)
                );
    }

    private static StateProducer state1_2(int lessonNb) {
        var o = new StateProducer(lessonNb);
        o.goals = List.of(1, 2);
        o.lockedStateElements = List.of();
        o.state = stateDone;
        o.episodesToTrain = 100;
        shuffle(o, o.lockedStateElements);
        return o;
    }


    private static StateProducer state1(int lessonNb1) {
        return stateX(1, lessonNb1);
    }


    private static StateProducer state3_4(int lessonNb1) {
        var o = new StateProducer(lessonNb1);
        o.goals = List.of(3, 4);
        o.lockedStateElements = List.of(1, 2);
        o.state = stateDone;
        o.episodesToTrain = 100;

        shuffle(o, List.of(1, 2, 3));

        return o;
    }

    private static StateProducer state5_6(int lessonNb1) {
        var o = new StateProducer(lessonNb1);
        o.goals = List.of(5, 6);
        o.lockedStateElements = List.of(1, 2, 3, 4);
        o.state = stateDone;
        o.episodesToTrain = 100;

        shuffle(o, o.lockedStateElements);

        return o;
    }

    private static StateProducer stateX(int goal, int lessonNb) {
        var o = new StateProducer(lessonNb);
        o.goals = List.of(goal);
        o.lockedStateElements = IntStream.range(1, goal).boxed().collect(Collectors.toList());
        o.state = stateDone;
        o.episodesToTrain = 100;
        shuffle(o, o.lockedStateElements);
        return o;
    }

    private static StateProducer moveHole(StateProducer o, List<Integer> holeIndexes) {
        int ih = holeIndexes.stream().min(random()).orElseThrow();
        int i = o.state.indexOf(-1);
        int v = o.state.get(ih);
        o.state.set(ih, -1);
        o.state.set(i, v);
        return o;
    }


    private static StateProducer state7_8(int lessonNb1) {
        var o = new StateProducer(lessonNb1);
        o.goals = List.of(7, 8);
        o.lockedStateElements = List.of(1, 2, 3, 4, 5, 6);
        o.state = stateDone;
        o.episodesToTrain = 100;

        shuffle(o, List.of(1, 2, 3, 4, 5, 6, 7));
        return o;
    }

    private static StateProducer state9_13(int lessonNb1) {
        var o = new StateProducer(lessonNb1);
        o.goals = List.of(9, 13);
        o.lockedStateElements = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        o.state = stateDone;
        o.episodesToTrain = 100;
        shuffle(o, o.lockedStateElements);
        return o;
    }


    private static StateProducer state10_15(int lessonNb1) {
        var o = new StateProducer(lessonNb1);
        o.goals = List.of(10, 11, 14, 15);
        o.lockedStateElements = List.of(1, 2, 3, 4,
                5, 6, 7, 8,
                9,
                13);
        o.state = stateDone;
        o.state = StateShuffle.shuffle(o.state, o.lockedStateElements, 500);
        o.episodesToTrain = 100;
        return o;
    }

    private static StateProducer state12(int lessonNb1) {
        var o = new StateProducer(lessonNb1);
        o.goals = List.of(12);
        o.lockedStateElements = List.of(1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11,
                13, 14, 15);
        o.state = stateDone;
        o.state = StateShuffle.shuffle(o.state, o.lockedStateElements, 500);
        o.episodesToTrain = 10;
        return o;
    }


    public boolean isLockedIndex(int index) {
        int v = index + 1;
        return lockedStateElements.contains(v);
    }

    public void shuffleState() {
        state = StateShuffle.shuffle(state, lockedStateElements, 500);
    }

    private static void shuffle(StateProducer o, List<Integer> lockedStateElements) {
        List<Integer> v0 = new ArrayList<>(o.state);
        v0.removeAll(lockedStateElements);
        Collections.shuffle(v0);
        List<Integer> v1 = new ArrayList<>(lockedStateElements);
        v1.addAll(v0);
        o.state = v1;
    }

    public void resetState() {
        StateProducer o = generateLessons().get(lessonNb);
        goals = new ArrayList<>(o.goals);
        lockedStateElements = new ArrayList<>(o.lockedStateElements);
        state = new ArrayList<>(o.state);
    }
}

