package com.hsenid.calculator;

import static java.lang.Math.*;

/**
 * Created by 000407 on 12/1/2016.
 */
public class Math {
    //Conversions
    public static double toRadians(double degrees) {
        return PI * degrees / 180;
    }

    public static double toDegrees(double radians) {
        return radians * 180 / PI;
    }

    //Permutations & Combinations
    public static long factorial(long n) throws RuntimeException {
        if (n < 0)
            throw new RuntimeException("Invalid number!");
        if (n <= 1)
            return 1;
        return n * factorial(n - 1);
    }

    public static long permute(long n, long r) {
        return factorial(n) / factorial(n - r);
    }

    public static long combine(long n, long r) {
        return factorial(n) / (factorial(n - r) * factorial(r));
    }

    //Power & logarithms
    public static double power(double base, double exp) {
        return pow(base, exp);
    }

    public static double logarithm(double base, double num) {
        return (log(num) / log(base));
    }

    public static double logarithm(double num) {
        return (log(num) / log(10));
    }

    public static String squareRoot(double x) {
        String num = String.valueOf(sqrt(abs(x)));
        if (x < 0.0)
            num = num.concat("i");
        return num;
    }

    //Trigonometric functions
    public static double sine(double a) {
        return sin(a);
    }

    public static double tangent(double a) {
        return tan(a);
    }

    public static double secant(double a) {
        return 1 / cos(a);
    }

    public static void main(String[] args) {
        System.out.println(secant(toRadians(45)));
    }
}
