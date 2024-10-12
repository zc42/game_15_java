package com.zc.utils;


/*
class MinMax():
    def __init__(self, l=[]):
        self.oldmin = min(l)
        self.oldmax = max(l)
        self.oldrange = self.oldmax - self.oldmin
        self.newmin = 0.
        self.newmax = 1.
        self.newrange = self.newmax - self.newmin

    def min_max(self, v):
        if self.oldrange == 0:  # Deal with the case where rvalue is constant:
            if self.oldmin < self.newmin:  # If rvalue < newmin, set all rvalue values to newmin
                newval = self.newmin
            elif self.oldmin > self.newmax:  # If rvalue > newmax, set all rvalue values to newmax
                newval = self.newmax
            else:  # If newmin <= rvalue <= newmax, keep rvalue the same
                newval = self.oldmin
            normal = newval
        else:
            scale = self.newrange / self.oldrange
            normal = (v - self.oldmin) * scale + self.newmin

        return normal

 */

import java.util.DoubleSummaryStatistics;
import java.util.List;

public class MinMaxScaler {

    private final double newmin;
    private final double newmax;
    private final double oldmin;
    private final double oldmax;
    private final double oldrange;
    private final double newrange;

    public static MinMaxScaler from(List<Double> l) {
        DoubleSummaryStatistics stats = l.stream().mapToDouble(e -> e).summaryStatistics();
        return new MinMaxScaler(stats.getMin(), stats.getMax(), 0, 1);
    }

    public static MinMaxScaler from(List<Double> l, double newmin, double newmax) {
        DoubleSummaryStatistics stats = l.stream().mapToDouble(e -> e).summaryStatistics();
        return new MinMaxScaler(stats.getMin(), stats.getMax(), newmin, newmax);
    }

    public MinMaxScaler(double oldmin, double oldmax, double newmin, double newmax) {
        this.newmin = newmin;
        this.newmax = newmax;
        this.oldmin = oldmin;
        this.oldmax = oldmax;
        this.oldrange = oldmax - oldmin;
        this.newrange = newmax - newmin;
    }

    public double scale(double v) {
        return oldrange == 0 ? scaleWhenRangeIsConstant() : simpleScale(v);
    }

    private double simpleScale(double v) {
        double scale = newrange / oldrange;
        return (v - oldmin) * scale + newmin;
    }

    /*
    Deal with the case where rvalue is constant:
    -------------------------------------------------
    If rvalue < newmin, set all rvalue values to newmin
    If rvalue > newmax, set all rvalue values to newmax
    If newmin <= rvalue <= newmax, keep rvalue the same
     */
    private double scaleWhenRangeIsConstant() {
        return oldmin < newmin
                ? newmin
                : oldmin > newmax
                ? newmax
                : oldmin;
    }
}


