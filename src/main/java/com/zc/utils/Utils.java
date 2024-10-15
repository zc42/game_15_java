package com.zc.utils;

import lombok.SneakyThrows;
//import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Utils {

    public static void prnt(Object o) {
        System.out.println(o);
    }

    private static String emptyString = "                                           ";

    public static String str(Object o, int len) {
        String v = o.toString();
        if (v.length() >= len) return v;
        int i = len - v.length();
        return v + emptyString.substring(0, i);
    }

    public static double sum(List<Double> l) {
        if (l == null) {
            return 0D;
        }

        l = l.stream()
                .filter(e -> e != 0)
                .collect(toList());

        if (l.isEmpty()) {
            return 0D;
        }

        return l.stream().reduce(0D, Double::sum, px());
    }

    public static <T> String toString(Collection<T> list) {
        String collect = list.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "[" + collect + "]";
    }

    public static void prntln(Object msg) {
        System.out.println(msg);
    }


    public static <T> BinaryOperator<T> px() {
        return (t, u) -> t;
    }

    @SneakyThrows
    public static void writeTextToFile(String fileName, String content) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void _prnt(Object o) {
        System.out.print(o);
    }
}
