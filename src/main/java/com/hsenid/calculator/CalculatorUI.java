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
public class CalculatorUI extends Frame{
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
    private JButton btnShift;
    private JButton btnPermut;
    private JButton btnHex;
    private JList<Object> listHistory;
    private DefaultListModel<String> listHistoryArray;

    //Input string manipulation pointers
    private int currentLeft, currentRight;

    //Operation stacks: Declarations
    OpStack leftOperands, operators, rightOperands;

    //Operations
    private Map<Character, Integer> operations;

    private String parseNumber(){
        String num = txtInput.getText().substring(currentLeft, currentRight - 1);
        currentLeft = currentRight;
        return num;
    }

    private void evaluate(){
        double left = Double.parseDouble(leftOperands.pop()), right = Double.parseDouble(rightOperands.pop());
        String operator = operators.pop();
        switch(operator){
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
                try{
                    rightOperands.push(String.valueOf(left / right));
                }
                catch(ArithmeticException ae){
                    ae.printStackTrace();
                }
                break;

            case "%":
                rightOperands.push(String.valueOf(left * right / 100));
                break;

            case "(":
                // TODO: 12/2/16 Handle the end of parenthesis
                break;
            default:
                try{
                    throw new UnsupportedOperationException();
                }
                catch(UnsupportedOperationException uoe){
                    uoe.printStackTrace();
                }
        }
    }

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
        operations = new HashMap<>();
        operations.put('(', 5);
        operations.put(')', 5);
        operations.put('*', 4);
        operations.put('/', 4);
        operations.put('%', 4);
        operations.put('+', 2);
        operations.put('-', 2);
        operations.put('√', 4);

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
                while(!leftOperands.isEmpty()){
                    System.out.println("ExpEval...");
                    evaluate();
                }
                leftOperands.consoleLog();
                rightOperands.consoleLog();
                operators.consoleLog();
                listHistoryArray.insertElementAt(txtInput.getText(), listHistoryArray.getSize());
                listHistory.setListData(listHistoryArray.toArray());
            }
        });
    }

    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex){
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

    private class HistorySelectListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent e) {
            Object selected = listHistory.getSelectedValue();
            if(selected == null)
                return;
            txtInput.setText(selected.toString());
            listHistory.clearSelection();
        }
    }

    private class NumericPressListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            txtInput.setText(txtInput.getText().concat(e.getActionCommand()));
        }
    }

    private class ArithmeticOpsPressListener extends NumericPressListener {
        @Override
        public void actionPerformed(ActionEvent e){
            super.actionPerformed(e);
            //On-the-fly evaluation of expressions
            char operatorThis = e.getActionCommand().charAt(0);
            while(true){
                //If the operators stack is empty. i.e no operations were started
                if(operators.isEmpty()){
                    System.out.println("beginning");
                    operators.push(String.valueOf(operatorThis));
                    leftOperands.push(parseNumber());
                    break;
                }

                else if(rightOperands.isEmpty()){
                    System.out.println("right operand");
                    rightOperands.push(parseNumber());
                }

                char previousOperator = operators.peek().charAt(0);
                System.out.println("else");
                //If this operator has equal or greater precedence than the previous
                if(operations.get(operatorThis) > operations.get(previousOperator)){
                    System.out.println("high precedence operator");
                    leftOperands.push(rightOperands.pop());
                    operators.push(String.valueOf(operatorThis));
                    break;
                }

                //Otherwise
                else{
                    System.out.println("equal/low precedence operator");
                    while(true){
                        if(operators.isEmpty() || (operators.peek().matches("[(]"))){
                            break;
                        }
                        evaluate();
                    }
                    leftOperands.push(rightOperands.pop());
                    operators.push(String.valueOf(operatorThis));
                    break;
                }
            }
            leftOperands.consoleLog();
            rightOperands.consoleLog();
            operators.consoleLog();
            // TODO: 12/2/16 Implement on-the-fly behavior of the operators
        }
    }
}
