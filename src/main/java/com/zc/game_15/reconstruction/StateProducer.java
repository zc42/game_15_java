package com.zc.game_15.reconstruction;

import com.zc.utils.Pair;
import com.zc.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.zc.utils.Pair.P;
import static com.zc.utils.RandomComparator.random;
import static com.zc.utils.Utils.prnt;

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
                        moveHoleToStartAtPosition(stateX(2, 1), List.of(1, 4)),
                        moveHoleToStartAtPosition(stateX(3, 2), List.of(2, 5)),
                        moveHoleToStartAtPosition(state3_4(3), List.of(3, 6)),
                        moveHoleToStartAtPosition(stateX(5, 4), List.of(6, 7)),
                        moveHoleToStartAtPosition(stateX(6, 5), List.of(5, 8)),
                        moveHoleToStartAtPosition(stateX(7, 6), List.of(6, 9)),
                        moveHoleToStartAtPosition(state7_8(7), List.of(8, 11)),
                        moveHoleToStartAtPosition(state9_13(8), List.of(10, 11)),
                        moveHoleToStartAtPosition(state10_15(9), List.of(9, 13)),
                        state12(10)
                );
    }

    public static List<StateProducer> generateLessonsV1() {
        return List.of
                (
                        state1_2(0),
                        moveHoleToStartAtPosition(state3_4(1), List.of(2, 3, 4)),
                        moveHoleToStartAtPosition(stateX(5, 2), List.of(6, 7)),
                        moveHoleToStartAtPosition(stateX(6, 3), List.of(5, 8)),
                        moveHoleToStartAtPosition(stateX(7, 4), List.of(6, 9)),
                        moveHoleToStartAtPosition(state7_8(5), List.of(8, 11)),
                        moveHoleToStartAtPosition(state9_13(6), List.of(10, 11)),
                        moveHoleToStartAtPosition(state10_15(7), List.of(9, 13)),
                        state12(8)
                );
    }

    public static List<StateProducer> generateLessons() {
        return List.of
                (
                        state1_2(0),
                        moveHoleToStartAtPosition(state3_4(1), List.of(2, 3, 4)),
                        moveHoleToStartAtPosition(state5_6(2), List.of(6, 7)),
                        moveHoleToStartAtPosition(state7_8(3), List.of(8, 9, 6)),
                        moveHoleToStartAtPosition(state9_13(4), List.of(10, 11)),
                        moveHoleToStartAtPosition(state10_15(5), List.of(9, 13)),
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

        //cia kazkoks keistas case'as padarytas , nes o.lockedStateElements = List.of(1, 2); ir visur naudaojama po to sitas
        //o cia sitaip 1,2,3
        //shuffle(o, List.of(1, 2, 3));
        //nu normaliai ir su [1, 2] kaip ir (lyg ir).. nepamenu case'o kur tas 1,2,3 butu reikalingas .. gliukas gal ..
        //gal tiesiog developinant pradzioj buvo kazkas neaisku su situo case'u .. nes cia 3, 4
        shuffle(o, o.lockedStateElements);

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

    private static StateProducer moveHoleToStartAtPosition(StateProducer o, List<Integer> holeIndexes) {
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
        o.lockedStateElements = List.of
                (
                        1, 2, 3, 4,
                        5, 6
                );
        o.state = stateDone;
        o.episodesToTrain = 100;

        shuffle(o, List.of
                (
                        1, 2, 3, 4,
                        5, 6, 7
                )
        );
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

    public static void main(String[] args) {
        shuffle(List.of(1, 2, 3, 4, 5, 6));
    }

    public static List<Integer> shuffle(List<Integer> lockedStateElements) {
        List<Integer> freeElements = IntStream.rangeClosed(1, 16).boxed()
                .filter(e -> !lockedStateElements.contains(e))
                .peek(Utils::prnt)
                .collect(Collectors.toList());

        prnt("---");
        List<Integer> randomFreeElements = freeElements.stream()
                .sorted(random())
                .peek(Utils::prnt)
                .collect(Collectors.toList());

        prnt("---");
        List<Pair<Integer, Integer>> paired = IntStream.range(0, freeElements.size()).boxed()
                .map(e -> P(freeElements.get(e), randomFreeElements.get(e)))
                .peek(Utils::prnt)
                .collect(Collectors.toList());

        prnt("---");

        List<Pair<Integer, Integer>> shuffledBoardState = IntStream.range(0, 16).boxed()
                .map(e -> zip(paired, e))
                .peek(Utils::prnt)
//                .peek(e->prnt(e.getValue()))
                .collect(Collectors.toList());

        return shuffledBoardState.stream()
                .map(Pair::getValue)
                .map(e -> e == 16 ? -1 : e)
                .peek(Utils::prnt)
                .collect(Collectors.toList());
    }

    private static Pair<Integer, Integer> zip(List<Pair<Integer, Integer>> paired, int index) {
        Optional<Pair<Integer, Integer>> pairOption = paired.stream().filter(e -> e.getKey() == index + 1).findFirst();
        if (pairOption.isEmpty()) return P(index, index + 1);
        else return pairOption.map(e -> P(index, e.getValue())).orElseThrow();
    }


    /*
1 2 3 4
5 6 7 8
9 -1 14 10
13 11 12 16
     */

}

