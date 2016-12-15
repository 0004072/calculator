package com.hsenid.calculator;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by hsenid on 12/15/16.
 */
public class BaseConversions {
    private static Map<String, Pattern> basePatterns = new HashMap<>();

    public BaseConversions() {
        basePatterns.put("hex", Pattern.compile("[\\dabcde]+"));
        basePatterns.put("dec", Pattern.compile("[\\d]+"));
        basePatterns.put("oct", Pattern.compile("[0-8]+"));
        basePatterns.put("bin", Pattern.compile("[0-1]+"));
    }

    public String toBin(String input) {
        String base = getCurrentBase();
        if (!Pattern.matches(basePatterns.get(base).toString(), input)) {
            throw new InvalidNumberException("Invalid input string!");
        }

        String output = "";

        switch (base) {
            case "hex":
                output = Integer.toBinaryString(Integer.parseInt(input, 16));
                break;

            case "dec":
                output = Integer.toBinaryString(Integer.parseInt(input));
                break;

            case "oct":
                output = Integer.toBinaryString(Integer.parseInt(input, 8));
                break;

            case "bin":
                output = input;
                break;
        }

        return output;
    }

    public String toHex(String input) {
        String base = getCurrentBase();
        if (!Pattern.matches(basePatterns.get(base).toString(), input)) {
            throw new InvalidNumberException("Invalid input string!");
        }
        String output = "";
        switch (base) {
            case "hex":
                output = input;
                break;

            case "dec":
                output = Integer.toHexString(Integer.parseInt(input));
                break;

            case "oct":
                output = Integer.toHexString(Integer.parseInt(input, 8));
                break;

            case "bin":
                output = Integer.toHexString(Integer.parseInt(input, 2));
                break;
        }
        return output;
    }

    public String toDec(String input) {
        String base = getCurrentBase();
        if (!Pattern.matches(basePatterns.get(base).toString(), input)) {
            throw new InvalidNumberException("Invalid input string!");
        }

        String output = "";

        switch (base) {
            case "hex":
                output = String.valueOf(Integer.parseInt(input, 16));
                break;

            case "dec":
                output = input;
                break;

            case "oct":
                output = String.valueOf(Integer.parseInt(input, 8));
                break;

            case "bin":
                output = String.valueOf(Integer.parseInt(input, 2));
                break;
        }

        return output;
    }

    public String toOct(String input) {
        String base = getCurrentBase();
        if (!Pattern.matches(basePatterns.get(base).toString(), input)) {
            throw new InvalidNumberException("Invalid input string!");
        }

        String output = "";

        switch (base) {
            case "hex":
                output = Integer.toOctalString(Integer.parseInt(input, 16));
                break;

            case "dec":
                output = Integer.toOctalString(Integer.parseInt(input));
                break;

            case "oct":
                output = input;
                break;

            case "bin":
                output = Integer.toOctalString(Integer.parseInt(input, 2));
                break;
        }
        return output;
    }

    private String getCurrentBase() {
        String[] choices = {"hex", "dec", "oct", "bin"};
        return JOptionPane.showInputDialog(null, "What is the base of the value you have entered?", "Choose base...", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]).toString();
    }
}
