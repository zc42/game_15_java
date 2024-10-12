package com.zc.game_15.reconstruction;

import java.text.MessageFormat;
import java.util.stream.IntStream;

import static com.zc.utils.Utils._prnt;
import static com.zc.utils.Utils.prnt;

public class ConsoleUtils {

    public static String red(String s) {
        return ConsoleColors.RED + s + ConsoleColors.RESET;
    }

    public static String green(String s) {
        return ConsoleColors.GREEN + s + ConsoleColors.RESET;
    }

    public static String blue(String s) {
        return ConsoleColors.BLUE + s + ConsoleColors.RESET;
    }

    public static String blueBack(String s) {
        return ConsoleColors.BLUE_BACKGROUND + s + ConsoleColors.RESET;
    }

    public static String tealBack(String s) {
        return ConsoleColors.TEAL_BACKGROUND + s + ConsoleColors.RESET;
    }

    public static String pinkBack(String s) {
        return ConsoleColors.PINK_BACKGROUND + s + ConsoleColors.RESET;
    }


    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void clearScreen2() {
        IntStream.range(0, 100).boxed()
                .forEach(e -> System.out.print("\r"));
    }


    public static String color(String s, int i) {
        var r = 255;
        var g = 255;
        var b = i;
        return colorRGB(s, r, g, b);
    }

    public static String colorRGB(String s, int r, int g, int b) {
        var color = MessageFormat.format("\033[48;2;{0};{1};{2}m", r, g, b);
        return color + s + ConsoleColors.RESET;
    }

    public static void main(String[] args) {
        IntStream.range(0, 255).boxed().forEach(
                r -> {
                    IntStream.range(0, 255).boxed().forEach(e -> {
                        _prnt(colorRGB(" ", r, e, 100));
                    });
                    prnt("");
                });
    }
}
