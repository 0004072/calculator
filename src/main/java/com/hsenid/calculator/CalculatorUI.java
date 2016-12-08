package com.hsenid.calculator;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static com.hsenid.calculator.Math.*;
import static javax.swing.JOptionPane.*;

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
    private JButton btnPermute;
    private JButton btnHex;
    private JList<Object> listHistory;
    private JTextField txtOutput;
    private JButton btnPi;
    private JButton btnE;
    private DefaultListModel<String> listHistoryArray;

    //Input string manipulation pointers
    private int currentLeft, currentRight;

    //Operation stacks: Declarations
    private OpStack leftOperands, operators, rightOperands;

    //Operations & functions
    private Map<String, Integer> operatorPrecedence;
    private Set<String> functions;
    private Map<String, String> shiftPairs;

    //Memory: Variable declarations
    private Double mem, ans;

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
        operatorPrecedence.put("(", 6);
        operatorPrecedence.put(")", 7);
        operatorPrecedence.put("*", 4);
        operatorPrecedence.put("/", 4);
        operatorPrecedence.put("%", 4);
        operatorPrecedence.put("+", 2);
        operatorPrecedence.put("-", 2);
        operatorPrecedence.put("√", 5);
        operatorPrecedence.put("^", 5);
        operatorPrecedence.put("C", 4);
        operatorPrecedence.put("P", 4);
        operatorPrecedence.put("!", 4);

        //Functions list: Initialization
        functions = new HashSet<>();
        functions.add("Sin");
        functions.add("Cos");
        functions.add("Tan");
        functions.add("√");
        functions.add("!");

        //Shift pairs initializations
        shiftPairs = new HashMap<>();
        shiftPairs.put("Sin", "Cosec");
        shiftPairs.put("Cos", "Sec");
        shiftPairs.put("Tan", "Cot");

        //Action definition for the text change in the txtInput text field.
        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                currentRight = txtInput.getText().length();
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                //leftOperands.flush();
                //rightOperands.flush();
                //operators.flush();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
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
        btnPlusMinus.addActionListener(new NumericPressListener());
        btnPi.addActionListener(new NumericPressListener());
        btnE.addActionListener(new NumericPressListener());

        //Action definitions for arithmetic operator keys
        btnAdd.addActionListener(new ArithmeticOpsPressListener());
        btnSub.addActionListener(new ArithmeticOpsPressListener());
        btnMul.addActionListener(new ArithmeticOpsPressListener());
        btnDiv.addActionListener(new ArithmeticOpsPressListener());
        btnPercent.addActionListener(new ArithmeticOpsPressListener());
        btnParLeft.addActionListener(new ArithmeticOpsPressListener());
        btnParRight.addActionListener(new ArithmeticOpsPressListener());
        btnPower.addActionListener(new ArithmeticOpsPressListener());
        btnComb.addActionListener(new ArithmeticOpsPressListener());
        btnPermute.addActionListener(new ArithmeticOpsPressListener());

        //Action definitions for function keys
        btnSin.addActionListener(new FunctionPressListener());
        btnCos.addActionListener(new FunctionPressListener());
        btnTan.addActionListener(new FunctionPressListener());
        btnSqrt.addActionListener(new FunctionPressListener());
        btnFact.addActionListener(new FunctionPressListener());

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                txtInput.setText("");
                operators.flush();
                leftOperands.flush();
                rightOperands.flush();
                currentLeft = 0;
                currentRight = 0;
            }
        });

        btnEquals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                // TODO: 12/1/16 Implement validation prior to evaluation
                try {
                    if (txtInput.getText().length() > 0 && currentLeft == 0 && currentRight == 0) {
                        evaluateExpressionString(txtInput.getText());
                    } else {
                        if (txtInput.getText().matches("(.*)([\\d]+|π|e|ans)")) {
                            txtInput.setText(txtInput.getText().concat(" "));
                            rightOperands.push(parseNumber());
                        }

                        while (!leftOperands.isEmpty()) {
                            if (operators.isEmpty() && rightOperands.isEmpty()) {
                                rightOperands.push(leftOperands.pop());
                                break;
                            }
                            if (functions.contains(operators.peek())) {
                                rightOperands.push(leftOperands.pop());
                            }
                            evaluate();
                        }
                    }
                    traceStacks();
                    String result = rightOperands.pop();
                    String currentInput = txtInput.getText();

                    if (ans != null)
                        currentInput = currentInput.replaceAll("ans", ans.toString());

                    listHistoryArray.insertElementAt(currentInput.concat("=").concat(result), listHistoryArray.getSize());
                    listHistory.setListData(listHistoryArray.toArray());
                    txtOutput.setText(result);
                    txtInput.setText("");
                    ans = Double.parseDouble(result);
                    currentLeft = 0;
                    currentRight = 0;
                } catch (Exception e1) {
                    txtOutput.setText("Malformed expression!");
                    e1.printStackTrace();
                }
            }
        });

        btnMemAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String numInText = txtInput.getText();
                    if (numInText.equals("ans")) {
                        numInText = ans.toString();
                    }

                    if (mem == null)
                        mem = new Double(numInText);
                    else
                        mem += Double.parseDouble(numInText);
                } catch (Exception e) {
                    e.printStackTrace();
                    txtOutput.setText("Syntax error!");
                }
            }
        });

        btnMemSub.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    String numInText = txtInput.getText();
                    if (mem == null)
                        mem = new Double(numInText);
                    else
                        mem += Double.parseDouble(numInText);
                } catch (Exception e) {
                    e.printStackTrace();
                    txtOutput.setText("Syntax error!");
                }
            }
        });

        btnMemRC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);

                if (me.getClickCount() == 2) {
                    mem = null;
                } else if (me.getClickCount() == 1) {
                    if (mem != null)
                        txtInput.setText(txtInput.getText().concat(String.valueOf(mem)));
                    else
                        JOptionPane.showMessageDialog(null, "Memory is empty!", "Info", INFORMATION_MESSAGE);
                }
            }
        });

        btnAns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ans == null) {
                    JOptionPane.showMessageDialog(null, "No previous calculations!", "Info", INFORMATION_MESSAGE);
                    return;
                }
                txtInput.setText(txtInput.getText().concat("ans"));
            }
        });

        btnShift.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 12/7/16 Implement shift key behavior
            }
        });
    }

    public static void main(String[] args) {
        GridBagConstraints c = new GridBagConstraints();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("Calculator");
        frame.setJMenuBar(createMenuBar());
        frame.setSize(new Dimension(542, 357));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(new CalculatorUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //
    //UI Generation Methods
    //

    //Menu bar generation
    public static JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menuFile, menuEdit, menuView, subMenu1History;
        JMenuItem itmPreferences, itmLaunchPlotter, itmLoadHistory, itmSaveHistory, itmClearHistory;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //File menu.
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuFile.getAccessibleContext().setAccessibleDescription("File");
        menuBar.add(menuFile);

        //File menu items
        itmPreferences = new JMenuItem("Preferences");
        itmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        itmPreferences.getAccessibleContext().setAccessibleDescription("Set the preferences");
        menuFile.add(itmPreferences);

        //Edit menu.
        menuEdit = new JMenu("Edit");
        menuEdit.setMnemonic(KeyEvent.VK_E);
        menuEdit.getAccessibleContext().setAccessibleDescription("This menu does nothing");
        menuBar.add(menuEdit);

        //Sub Menu: History
        subMenu1History = new JMenu("History");

        //Menu items
        itmSaveHistory = new JMenuItem("Save...");
        itmSaveHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        subMenu1History.add(itmSaveHistory);

        itmLoadHistory = new JMenuItem("Load...");
        itmLoadHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        subMenu1History.add(itmLoadHistory);

        itmClearHistory = new JMenuItem("Clear");
        itmClearHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK));
        subMenu1History.add(itmClearHistory);

        menuEdit.add(subMenu1History);
        //Edit Menu: END

        //View Menu
        menuView = new JMenu("View");
        menuView.setMnemonic(KeyEvent.VK_V);
        menuView.getAccessibleContext().setAccessibleDescription("View");
        menuBar.add(menuView);

        itmLaunchPlotter = new JMenuItem("Launch plotter");
        itmLaunchPlotter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        menuView.add(itmLaunchPlotter);

        itmLaunchPlotter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Plotter.load();
            }
        });

        itmLoadHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(new JFrame(), "Choose history file", FileDialog.LOAD);
                fd.setDirectory("/home/hsenid/Documents");
                fd.setVisible(true);
                String filename = fd.getFile();
                if (filename != null && filename.endsWith(".txt")) {
                    //File open process
                    // TODO: 12/8/16 Complete this
                    System.out.println("valid file");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        itmSaveHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 12/8/16 Refine this
                FileDialog fd = new FileDialog(new JFrame(), "Save as..", FileDialog.SAVE);
                fd.setDirectory("/home/hsenid/Documents");
                fd.setVisible(true);
                String filename = fd.getFile();
                if (filename != null && filename.endsWith(".txt")) {
                    //File open process
                    System.out.println("valid file");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return menuBar;
    }

    //
    //Functional methods
    //
    private String parseNumber() {
        String num = txtInput.getText();
        num = num.substring(currentLeft, currentRight - 1);
        currentLeft = currentRight;
        if (num.equals("ans")) {
            if (ans != null) {
                return ans.toString();
            }

            num = "";
        }
        if (num.equals("e"))
            return String.valueOf(java.lang.Math.E);

        if (num.equals("π"))
            return String.valueOf(java.lang.Math.PI);

        return num;
    }

    private String evaluate() {
        // TODO: 12/8/16 Incorporate validations
        String operator = operators.pop();
        if (!operator.equals("(")) {
            if (functions.contains(operator)) {
                double left = Double.parseDouble(rightOperands.pop());
                switch (operator) {
                    case "Sin":
                        traceStacks();
                        rightOperands.push(String.valueOf(sine(toRadians(left))));
                        break;

                    case "Cos":
                        rightOperands.push(String.valueOf(1 / secant(toRadians(left))));
                        break;

                    case "Tan":
                        rightOperands.push(String.valueOf(tangent(toRadians(left))));
                        break;

                    case "√":
                        rightOperands.push(String.valueOf(squareRoot(left)));
                        break;

                    case "!":
                        long n = (long) left;
                        if (n != left)
                            break;
                        rightOperands.push(String.valueOf(factorial(n)));
                        break;
                }
            } else {
                double left = Double.parseDouble(leftOperands.pop());
                double right = Double.parseDouble(rightOperands.pop());
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

                    case "^":
                        rightOperands.push(String.valueOf(power(left, right)));
                        break;

                    case "C":
                        long nC = (long) left;
                        long rC = (long) right;
                        if (nC != left || rC != right) {
                            break;
                        }
                        rightOperands.push(String.valueOf(combine(nC, rC)));
                        break;

                    case "P":
                        long nP = (long) left;
                        long rP = (long) right;
                        if (nP != left || rP != right) {
                            break;
                        }
                        rightOperands.push(String.valueOf(combine(nP, rP)));
                        break;

                    default:
                        try {
                            throw new UnsupportedOperationException();
                        } catch (UnsupportedOperationException uoe) {
                            uoe.printStackTrace();
                        }
                }
            }
        }
        return operator;
    }

    private void evaluateExpressionString(String expressionString) {
        currentLeft = 0;
        currentRight = 0;

        for (; true; currentRight++) {
            if (currentRight == expressionString.length()) {
                break;
            }

            if (operatorPrecedence.containsKey(String.valueOf(expressionString.charAt(currentRight)))) {
                System.out.println(expressionString.charAt(currentRight));
                traverse(String.valueOf(expressionString.charAt(currentRight)));
            }
        }
    }

    private void traverse(String currentOperator) {
        try {
            if (currentOperator.equals(")")) {

                if (currentLeft != currentRight) {
                    String num = parseNumber();
                    if (!num.equals(""))
                        rightOperands.push(num);
                }
                traceStacks();
                currentRight++;
                currentLeft = currentRight;
                boolean openParenthesisFound = false;
                boolean thisIsNested = false;
                while (true) {
                    if (!leftOperands.isEmpty() && operators.isEmpty() && rightOperands.isEmpty())
                        break;

                        /*if (operators.isEmpty()) {
                            //Error conditions need to be handled

                            //If no errors
                            leftOperands.push(rightOperands.pop());
                            break;
                        }*/

                    if (!operators.isEmpty() && operators.peek().equals("(")) {
                        if (rightOperands.isEmpty()) {
                            rightOperands.push(leftOperands.pop());
                            //operators.pop();
                        }


                        if (leftOperands.isEmpty()) {
                            leftOperands.push(rightOperands.pop());
                            //operators.pop();
                            break;
                        }
                    }

                    if (leftOperands.isEmpty()) {
                        leftOperands.push(rightOperands.pop());
                        //operators.pop();
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
                        if (num.matches("^[\\d]+(\\.[\\d]+)?$"))
                            leftOperands.push(num);
                    }
                    operators.push(currentOperator);
                } else if (rightOperands.isEmpty()) {
                    String num = "";
                    if (currentLeft != currentRight)
                        num = parseNumber();

                    if (!num.matches("")) {
                        rightOperands.push(num);

                        String previousOperator = operators.peek();

                        if (operatorPrecedence.get(currentOperator) > operatorPrecedence.get(previousOperator)) {
                            leftOperands.push(rightOperands.pop());
                            operators.push(currentOperator);
                        } else {
                            while (true) {
                                if (!operators.isEmpty())
                                    previousOperator = operators.peek();
                                if (operators.isEmpty() || (operatorPrecedence.get(currentOperator) > operatorPrecedence.get(previousOperator))) {
                                    leftOperands.push(rightOperands.pop());
                                    operators.push(currentOperator);
                                    break;
                                }
                                if (previousOperator.equals("(")) {
                                    leftOperands.push(rightOperands.pop());
                                    operators.push(currentOperator);
                                    break;
                                }
                                evaluate();
                            }
                        }
                    } else
                        operators.push(currentOperator);
                }
            }
            if (!leftOperands.isEmpty())
                txtOutput.setText(leftOperands.peek());
        } catch (Exception e1) {
            txtOutput.setText("Malformed expression!");
            e1.printStackTrace();
        }
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
            currentLeft = 0;
            currentRight = 0;
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
            String currentOperator = e.getActionCommand();
            traverse(currentOperator);
        }
    }

    private class FunctionPressListener extends NumericPressListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            txtInput.setText(txtInput.getText().concat("("));
            operators.push(e.getActionCommand());
            operators.push("(");
            traceStacks();
            currentLeft = txtInput.getText().length();
        }
    }
}
