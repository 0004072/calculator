package com.hsenid.calculator;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by hsenid on 12/1/16.
 */
public class CalculatorUI extends Frame {
    private JPanel mainPanel;
    private JTextField txtInput;
    private JButton btnOct;
    private JButton btnDec;
    private JButton btnBin;
    private JButton btnMemAdd;
    private JButton btnMemSub;
    private JButton btnMemRC;
    private JButton btnClear;
    private JButton btnParLeft;
    private JButton btnParRight;
    private JButton btnAdd;
    private JButton btnSub;
    private JButton btnMul;
    private JButton btnDiv;
    private JButton btnPercent;
    private JButton btnAns;
    private JButton btnEquals;
    private JButton btn7;
    private JButton btn8;
    private JButton btn9;
    private JButton btn4;
    private JButton btn5;
    private JButton btn6;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JButton btn0;
    private JButton btnDot;
    private JButton btnPlusMinus;
    private JButton btnFact;
    private JButton btnSqrt;
    private JButton btnTan;
    private JButton btnCos;
    private JButton btnSin;
    private JButton btnLog;
    private JButton btnComb;
    private JButton btnPower;
    private JToggleButton btnShift;
    private JButton btnPermut;
    private JButton btnHex;
    private JList<Object> listHistory;
    private JTextField txtOutput;
    private DefaultListModel<String> listHistoryArray;

    //Input string manipulation pointers
    private int currentLeft, currentRight;

    //Operation stacks: Declarations
    private OpStack leftOperands, operators, rightOperands;

    //Operations & functions
    private Map<Character, Integer> operatorPrecedence;
    private Set<String> functions;

    public CalculatorUI() {
        //History list initializations
        currentLeft = 0;
        currentRight = 0;
        listHistoryArray = new DefaultListModel<>();
        listHistory.addListSelectionListener(new HistorySelectListener());

        //Operation stacks: Initializations
        leftOperands = new OpStack("left");
        rightOperands = new OpStack("right");
        operators = new OpStack("operators");

        //Operations map : Initialization
        operatorPrecedence = new HashMap<>();
        operatorPrecedence.put('(', 6);
        operatorPrecedence.put(')', 7);
        operatorPrecedence.put('*', 4);
        operatorPrecedence.put('/', 4);
        operatorPrecedence.put('%', 4);
        operatorPrecedence.put('+', 2);
        operatorPrecedence.put('-', 2);
        operatorPrecedence.put('√', 4);

        //Functions list: Initialization
        functions = new HashSet<>();

        //Action definition for the text change in the txtInput text field.
        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                currentRight = txtInput.getText().length();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //leftOperands.flush();
                //rightOperands.flush();
                //operators.flush();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // TODO: 12/2/16 implement the behavior when the input string is changed
            }
        });

        //Action definitions for numeric keys
        btn1.addActionListener(new NumericPressListener());
        btn2.addActionListener(new NumericPressListener());
        btn3.addActionListener(new NumericPressListener());
        btn4.addActionListener(new NumericPressListener());
        btn5.addActionListener(new NumericPressListener());
        btn6.addActionListener(new NumericPressListener());
        btn7.addActionListener(new NumericPressListener());
        btn8.addActionListener(new NumericPressListener());
        btn9.addActionListener(new NumericPressListener());
        btn0.addActionListener(new NumericPressListener());
        btnDot.addActionListener(new NumericPressListener());

        //Action definitions for arithmetic operator keys
        btnAdd.addActionListener(new ArithmeticOpsPressListener());
        btnSub.addActionListener(new ArithmeticOpsPressListener());
        btnMul.addActionListener(new ArithmeticOpsPressListener());
        btnDiv.addActionListener(new ArithmeticOpsPressListener());
        btnPercent.addActionListener(new ArithmeticOpsPressListener());
        btnParLeft.addActionListener(new ArithmeticOpsPressListener());
        btnParRight.addActionListener(new ArithmeticOpsPressListener());
        btnSqrt.addActionListener(new ArithmeticOpsPressListener());

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtInput.setText("");
                operators.flush();
                leftOperands.flush();
                rightOperands.flush();
                currentLeft = 0;
                currentRight = 0;
            }
        });

        btnEquals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: 12/1/16 Implement validation prior to evaluation
                txtInput.setText(txtInput.getText().concat("="));
                rightOperands.push(parseNumber());
                while (!leftOperands.isEmpty()) {
                    evaluate();
                }
                String result = rightOperands.pop();
                listHistoryArray.insertElementAt(txtInput.getText().concat(result), listHistoryArray.getSize());
                listHistory.setListData(listHistoryArray.toArray());
                txtOutput.setText(result);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("Calculator");
        frame.setSize(new Dimension(542, 357));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(new CalculatorUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private String parseNumber() {
        String num = txtInput.getText();
        num = num.substring(currentLeft, currentRight - 1);
        currentLeft = currentRight;
        return num;
    }

    private String evaluate() {
//        System.out.println("eval...");
//        leftOperands.consoleLog();
//        operators.consoleLog();
//        rightOperands.consoleLog();
        double left = Double.parseDouble(leftOperands.pop());
        double right = Double.parseDouble(rightOperands.pop());
        String operator = operators.pop();
        switch (operator) {
            case "+":
                rightOperands.push(String.valueOf(left + right));
                break;

            case "-":
                rightOperands.push(String.valueOf(left - right));
                break;

            case "*":
                rightOperands.push(String.valueOf(left * right));
                break;

            case "/":
                try {
                    rightOperands.push(String.valueOf(left / right));
                } catch (ArithmeticException ae) {
                    ae.printStackTrace();
                }
                break;

            case "%":
                rightOperands.push(String.valueOf(left * right / 100));
                break;

            case "(":
                // TODO: 12/2/16 Handle the end of parenthesis.
                leftOperands.push(String.valueOf(left));
                rightOperands.push(String.valueOf(right));
                break;

            default:
                try {
                    throw new UnsupportedOperationException();
                } catch (UnsupportedOperationException uoe) {
                    uoe.printStackTrace();
                }
        }
        return operator;
    }

    //Test methods
    public void traceStacks() {
        leftOperands.consoleLog();
        operators.consoleLog();
        rightOperands.consoleLog();
    }

    private class HistorySelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            Object selected = listHistory.getSelectedValue();
            if (selected == null)
                return;
            txtInput.setText(selected.toString().split("=")[0]);
            listHistory.clearSelection();
        }
    }

    private class NumericPressListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            txtInput.setText(txtInput.getText().concat(e.getActionCommand()));
        }
    }

    private class ArithmeticOpsPressListener extends NumericPressListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);

            //On-the-fly evaluation of expressions
            char currentOperator = e.getActionCommand().charAt(0);

            if (currentOperator == ')') {

                if (currentLeft != currentRight) {
                    String num = parseNumber();
                    if (!num.equals(""))
                        rightOperands.push(num);
                }

                currentRight++;
                currentLeft = currentRight;
                boolean openParenthesisFound = false;
                boolean thisIsNested = false;
                while (true) {
                    if (operators.isEmpty()) {
                        //Error conditions need to be handled

                        //If no errors
                        leftOperands.push(rightOperands.pop());
                        break;
                    }

                    String operationCompleted = evaluate();

                    if (openParenthesisFound) {
                        if (operationCompleted.equals("("))
                            thisIsNested = true;
                    }

                    if (openParenthesisFound && thisIsNested) {
                        leftOperands.push(rightOperands.pop());
                        operators.push("(");
                        break;
                    }

                    if (operationCompleted.equals("("))
                        openParenthesisFound = true;
                }
            } else {
                if (operators.isEmpty()) {
                    if (currentLeft != currentRight) {
                        String num = parseNumber();
                        if (!num.equals(""))
                            leftOperands.push(num);
                    }
                    operators.push(String.valueOf(currentOperator));
                } else if (rightOperands.isEmpty()) {
                    String num = "";
                    if (currentLeft != currentRight)
                        num = parseNumber();

                    if (!num.matches("")) {
                        rightOperands.push(num);

                        char previousOperator = operators.peek().charAt(0);

                        if (operatorPrecedence.get(currentOperator) > operatorPrecedence.get(previousOperator)) {
                            leftOperands.push(rightOperands.pop());
                            operators.push(String.valueOf(currentOperator));
                        } else {
                            while (true) {
                                if (!operators.isEmpty())
                                    previousOperator = operators.peek().charAt(0);
                                if (operators.isEmpty() || (operatorPrecedence.get(currentOperator) > operatorPrecedence.get(previousOperator))) {
                                    leftOperands.push(rightOperands.pop());
                                    operators.push(String.valueOf(currentOperator));
                                    break;
                                }
                                if (previousOperator == '(') {
                                    leftOperands.push(rightOperands.pop());
                                    operators.push(String.valueOf(currentOperator));
                                    break;
                                }
                                evaluate();
                            }
                        }
                    } else
                        operators.push(String.valueOf(currentOperator));
                }
            }
            txtOutput.setText(leftOperands.peek());
            traceStacks();
        }
    }
}
