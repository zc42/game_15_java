package com.zc.game_15.reconstruction;

import java.util.Map;
public class QTableUpdater {

    public static void updateQTable
            (
                    Map<Integer, QTableRow> qTable,
                    EnvironmentState state0,
                    Action action,
                    EnvironmentState state1,
                    double reward,
                    boolean isTerminal,
                    double learning_rate,
                    double discount
            ) {
        double _qValue;
        if (isTerminal) _qValue = reward;
        else {
            double qValue = getQValue(qTable, state0, action);
            double nextQValue = getMaxQValue(qTable, state1);
            _qValue = calcQValue(reward, qValue, nextQValue, discount, learning_rate);
        }
        addStateWithZeroValuesToQTableIfStateNotExist(qTable, state0);
        updateQTable(qTable, state0.getHashCodeV2(), action, _qValue);
    }

    private static void updateQTable(Map<Integer, QTableRow> qTable, int hash, Action action, double qValue) {
        qTable.get(hash).setValue(action, qValue);
    }

    private static Double getMaxQValue(Map<Integer, QTableRow> qTable, EnvironmentState state) {
        Integer hashCode = state.getHashCodeV2();
        addStateWithZeroValuesToQTableIfStateNotExist(qTable, state);
        return qTable.get(hashCode).getMaxValue();
    }

    private static Double getQValue(Map<Integer, QTableRow> qTable, EnvironmentState state, Action action) {
        Integer hashCode = state.getHashCodeV2();
        addStateWithZeroValuesToQTableIfStateNotExist(qTable, state);
        return qTable.get(hashCode).getValue(action);
    }

    public static void addStateWithZeroValuesToQTableIfStateNotExist(Map<Integer, QTableRow> qTable, EnvironmentState state) {
        if (qTable.containsKey(state.getHashCodeV2())) return;
        addStateWithZeroValuesToQTable(qTable, state);
    }
//    public static void _addStateWithZeroValuesToQTableIfStateNotExist(Map<String, Action> qTable, EnvironmentState state) {
//        String key = state.getHashCodeV3__();
//        if (qTable.containsKey(key)) return;
//        qTable.put(key, Action.L);
//    }

    private static void addStateWithZeroValuesToQTable(Map<Integer, QTableRow> qTable, EnvironmentState state) {
        Integer hashCode = state.getHashCodeV2();
        qTable.put(hashCode, QTableRow.of(state));
    }


    private static double calcQValue
            (
                    double reward,
                    double qValue,
                    double nextQValue,
                    double discount,
                    double learning_rate
            ) {
        double v = qValue
                + learning_rate
                * (reward + discount * nextQValue - qValue);
        return v;
    }

}
