package com.zc.game_15;

import com.zc.game_15.reconstruction.Action;
import com.zc.game_15.reconstruction.QTableRow;
import com.zc.utils.Utils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static com.zc.game_15.reconstruction.QTableGenerator.loadQTable;
import static com.zc.utils.Utils.prnt;

public class QTableToCsv {

    public static void main(String[] args) {
        String qTableFileName = "qTable.ser";
        String csvFileName = "qTableActions.csv";
        HashMap<Integer, QTableRow> qTable = loadQTable(qTableFileName);
        List<String> csvData = getCsvData(qTable);
        String content = csvData.stream()
                .sorted()
                .peek(e->prnt(MessageFormat.format("{0}  ", e)))
                .collect(Collectors.joining("\n"));
        Utils.writeTextToFile(csvFileName, content);
    }

    public static Map<String, Action> convertTable(HashMap<Integer, QTableRow> qTable) {
        Map<String, Action> map=new HashMap<>();
        qTable.values().stream()
                .map(QTableToCsv::getCsvRow)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(e->e.split(","))
                .forEach(e->map.put(e[0], Action.valueOf(e[1])));
        return map;
    }

    private static List<String> getCsvData(HashMap<Integer, QTableRow> qTable) {
        return qTable.values().stream()
                .map(QTableToCsv::getCsvRow)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Optional<String> getCsvRow(QTableRow row) {
        if (row.qValues.isEmpty()) return Optional.empty();

        Action action = row.qValues.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow();

//        EnvironmentState state = row.getState();
        String r0 = getHashCodeV3__(row);
        String r1 = action.name();
        String format = MessageFormat.format("{0},{1}", r0, r1);
        return Optional.of(format);
    }

    public static String getHashCodeV3__(QTableRow row) {
        List<Integer> state = row.getState().getState();
        List<Integer> goals = row.getState().getGoals();
        return IntStream.range(0, 16).boxed()
                .map(e -> {
                    String v;
                    Integer o = state.get(e);
                    if (o == -1) v = "*";
                    else if (goals.contains(o)) v = String.valueOf(o);
                    else if (goals.contains(e + 1)) v = "o";
                    else v = " ";
                    v = v + "_";
                    if (e != 0 && (e + 1) % 4 == 0) v = v + "|";
                    return v;
                })
                .collect(Collectors.joining(""));
    }

}
