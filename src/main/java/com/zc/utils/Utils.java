package com.zc.utils;


import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
//import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.*;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.zc.utils.Pair.P;
import static java.util.stream.Collectors.toList;

public class Utils {
    public static <T> Stream<Pair<Integer, T>> zipWithIndex(List<T> list) {
        return zipWithIndex(list.stream());
    }

    public static <T> Stream<Pair<Integer, T>> zipWithIndex(Stream<T> stream) {
        PlusPlus pp = PlusPlus.create();
        return stream.map(e -> P(pp.plusOne(), e));
    }

    @NoArgsConstructor(staticName = "create")
    public static class PlusPlus {
        int i = 0;

        public int plusOne() {
            return i++;
        }
    }

    public static <T> Stream<Pair<T, T>> zipWithPrev(Stream<T> stream) {
        PrevValueMapper<T> mapper = PrevValueMapper.create();
        return stream
                .map(mapper::pairWithPrev)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    @NoArgsConstructor(staticName = "create")
    public static class PrevValueMapper<T> {
        private Optional<T> prev = Optional.empty();

        public Optional<Pair<T, T>> pairWithPrev(T value) {
            Optional<Pair<T, T>> pair = prev.map(e -> P(e, value));
            prev = Optional.of(value);
            return pair;
        }
    }

    public static double[] softmax(double[] input) {
        double[] result = new double[input.length];
        double sum = 0.0;

        // Calculate the exponential of each element
        for (int i = 0; i < input.length; i++) {
//            result[i] = Math.exp(input[i]);
            result[i] = input[i];
            sum += result[i];
        }

        // Normalize the values to get the probabilities
        for (int i = 0; i < result.length; i++) {
            result[i] /= sum;
        }

        return result;
    }


    public static <T> Stream<T> createStream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.IMMUTABLE), false);
    }

    public static final String resourceFolder = getWorkingDir() + "/resources/";
    public static final ZoneId UTC_id = ZoneId.of("UTC");

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

    public static String strb(Object o, int len) {
        String v = o.toString();
        if (v.length() >= len) return v;
        int i = len - v.length();
        return emptyString.substring(0, i) + v;
    }


    public static double round(double a) {
        return (double) Math.round(a * 100) / 100;
    }

    public static double floor(double a, int k) {
        final double prczn = Math.pow(10, k);
        double r = Math.floor(a * prczn) / prczn;
//        double r2 = Math.round(a * prczn) / prczn;
//
//        double aaa1 = r + 0.000005;
//        double aaa2 = r2 + 0.000005;
//
//        double v1 = Math.floor(aaa1 * prczn) / prczn;
//        double v2 = Math.round(aaa2 * prczn) / prczn;

        return r;
    }

    public static double round(double a, int k) {
        final double prczn = Math.pow(10, k);
        double r = Math.round(a * prczn) / prczn;
        return r;
    }

    public static String frmt(double v) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(v);
//        double finalValue = (Double) df.parse(formate);
    }

    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    public static <T> List<T> filterByDate(List<T> l,
                                           Function<T, LocalDateTime> f,
                                           LocalDateTime fromD,
                                           LocalDateTime tillD) {
        if (fromD != null) {
            l = l.stream()
                    .filter(e -> f.apply(e).isEqual(fromD)
                            || f.apply(e).isAfter(fromD))
                    .collect(toList());

        }
        if (tillD != null) {
            l = l.stream()
                    .filter(e -> f.apply(e).isBefore(tillD))
                    .collect(toList());
        }
        return l;
    }

    public static void sleep(Duration duration) {
        prntln(MessageFormat.format("sleep: {0}", duration));
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static double percentage(double buy, double sell) {
        return ((sell - buy) / buy) * 100;
    }

    public static float percentageF(float buy, float sell) {
        return ((sell - buy) / buy) * 100;
    }

    public static double percentage2(double total, double value) {
        return (value / total) * 100;
    }

    public static double percentage1(double total, double count) {
        return ((total - count) / total) * 100;
    }

    public static double percentageOf(double total, double percentage) {
        return percentage * total / 100;
    }

    public static Double sumReturns(List<Double> returns, double initialSum) {
        return returns.stream().reduce(initialSum, (a, b) -> a + percentageOf(a, b), px());
    }

    public static double calcProfit(List<Double> returns) {
        double initialSum = 1;
        Double sumReturns = sumReturns(returns, initialSum);
        return percentage(initialSum, sumReturns);
    }

    public static Pair<Double, Double> calcProfit(double v1, double v2) {
        return P(v2 - v1, percentage(v1, v2));
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

//        int size = l.size();
        Double p = l.stream()
                .reduce(0D, (t, u) -> t + u, px());
        return p;
//        return p / size;

    }

    public static double sum_minus_tax(List<Double> l, int tradeCount, double d) {
        double r = sum(l) - (tradeCount * d);
        return r;
    }

    public static String d2s(Double v) {
        return v == null ? null : String.valueOf(v);

    }

    public static Double s2D(String v) {
        return v == null || v.isEmpty()
                ? null
                : Double.valueOf(v);
    }

    public static LocalDateTime long2LDT(long v) {
        Instant i = Instant.ofEpochMilli(v);
        return LocalDateTime.ofInstant(i, ZoneId.of("UTC"));
    }

    public static ZonedDateTime long2ZDT(long v) {
        Instant i = Instant.ofEpochMilli(v);
        return ZonedDateTime.ofInstant(i, ZoneId.of("UTC"));
    }

    public static long LDT2Long(LocalDateTime fromD) {
        return fromD.toInstant(ZoneOffset.UTC).getEpochSecond() * 1000;
    }

    public static long ZDT2Long(ZonedDateTime fromD) {
        return fromD.toInstant().getEpochSecond() * 1000;
    }

    public static ZonedDateTime getZonedDate(LocalDate d1) {
        return ZonedDateTime.of(d1, LocalTime.MIN, ZoneId.of("UTC"));
    }

    public static ZonedDateTime getZonedDate(LocalDateTime d1) {
        return ZonedDateTime.of(d1, ZoneId.of("UTC"));
    }

    public static double get_pct(Double v, Double pct) {
        return v * pct / 100D;
    }

//    public static float[] toFloatArray(List<Double> list, int valueForNull) {
//        return ArrayUtils.toPrimitive(list.stream().map(Double::floatValue).toArray(Float[]::new), valueForNull);
//    }

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
