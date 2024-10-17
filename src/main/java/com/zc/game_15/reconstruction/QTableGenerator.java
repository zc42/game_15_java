package com.zc.game_15.reconstruction;

import com.zc.utils.SerializedObjectLoader;
import lombok.SneakyThrows;

import java.util.*;

import static com.zc.game_15.reconstruction.GameUtils.makeMove;
import static com.zc.utils.RandomComparator.random;
import static com.zc.utils.Utils.prntln;

public class QTableGenerator {


    public static void train() {
        String filePath = "qTable.ser";
        HashMap<Integer, QTableRow> qTable = loadQTable(filePath);
        Trainer.train(qTable, filePath, 10);
    }

    public static void test() {
        String filePath = "qTable.ser";
        HashMap<Integer, QTableRow> qTable = loadQTable(filePath);
        while (true) {
            test(qTable);
        }
    }

    public static HashMap<Integer, QTableRow> loadQTable(String filePath) {
        var qTable = new HashMap<Integer, QTableRow>();
        try {
            qTable = SerializedObjectLoader.load(filePath);
        } catch (Exception exception) {
            prntln(exception);
        }

        DoubleSummaryStatistics stats = qTable.values().stream()
                .flatMap(e -> e.getQValues().values().stream())
                .mapToDouble(e -> e)
                .summaryStatistics();

        prntln(stats);

        return qTable;
    }

    @SneakyThrows
    public static void test(Map<Integer, QTableRow> qTable) {

        prntln("********************* test q table **********************");
        prntln("********************* test q table **********************");
        prntln("********************* test q table **********************");

        int lessonNo = 0;
        List<StateProducer> lessons = StateProducer.generateLessons();
        int lessonCount = lessons.size();
        StateProducer stateProducer = lessons.get(lessonNo);
        List<Integer> v = StateShuffle.shuffle(StateProducer.stateDone, List.of(), 1000);
        EnvironmentState state = new EnvironmentState(v, stateProducer);
        List<Integer> goals = stateProducer.getGoals();

        prntState(state);

        boolean gameOver = false;
        int step = 0;
        Action reverseAction = null;
        while (!gameOver && step < 200) {

            Thread.sleep(1000 / 2);
            ConsoleUtils.clearScreen();

            step++;
            Action action;
            Integer state0Hash = state.getHashCodeV2();


            QTableUpdater.addStateWithZeroValuesToQTableIfStateNotExist(qTable, state);

            QTableRow qTableRow = qTable.get(state0Hash);
            action = qTableRow.getActionWithMaxValue(reverseAction);

            reverseAction = GameUtils.getReverseAction(action);

            List<Integer> newState = makeMove(state.getState(), action);
            boolean isTerminal = Environment.isTerminalSuccess(newState, goals);

            state = new EnvironmentState(newState, stateProducer);
            gameOver = state.getState().equals(StateProducer.stateDone);

            prntln(step + "\n----\n");
            prntState(state);

            if (isTerminal && !gameOver && lessonNo < lessonCount - 1) {
                lessonNo++;
                stateProducer = lessons.get(lessonNo);
                goals = stateProducer.getGoals();
                prntln("lesson change: " + lessonNo);
                prntln(goals);
                state = new EnvironmentState(state.getState(), stateProducer);
            }
        }

        boolean isTerminalSuccess = Environment.isTerminalSuccess(state);
        prntln("success: " + isTerminalSuccess);
        Thread.sleep(3000);
    }

    private static void prntState(EnvironmentState state) {
        String s = GameUtils.stateAsString(state.getState(), state.getGoals());
        prntln(s);
    }

    public static Action getAction(Map<Integer, QTableRow> qTable, EnvironmentState currentState, Action lastAction) {
        Integer hash = currentState.getHashCodeV2();
        List<Action> possibleActions = Environment.getPossibleActions(currentState);
        possibleActions.remove(lastAction);
        return qTable.containsKey(hash)
                ? qTable.get(hash).getActionWithMaxValue(lastAction)
                : getRandomAction(possibleActions);
    }

    public static Action getRandomAction(List<Action> possibleActions) {
        return possibleActions.stream().min(random()).orElse(Action.D);
    }

}
