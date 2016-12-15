package com.hsenid.calculator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.hsenid.calculator.Math.*;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

/**
 * CalculatorUI Class:
 * Created by 000407 on 12/1/16.
 */
public class CalculatorUI extends Frame {
    //Logger
    private static Logger logger;
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
    //History data
    private DefaultListModel<String> listHistoryArray;
    private History history;
    //Input string manipulation pointers
    private int currentLeft, currentRight;
    //Operation stacks: Declarations
    private OpStack leftOperands, operators, rightOperands;
    //Operations & functions
    private Map<String, Integer> operatorPrecedence;
    private Set<String> functions;
    private Map<String, String> shiftPairs;
    //Base Conversions
    private BaseConversions baseConversions;
    //Memory: Variable declarations
    private Double mem;
    private String ans;

    public CalculatorUI() {
        //Expression string traversal parameters
        currentLeft = 0;
        currentRight = 0;

        //History list initializations
        listHistoryArray = new DefaultListModel<>();
        listHistory.addListSelectionListener(new HistorySelectListener());
        history = new History();

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
        functions.add("log");
        functions.add("√");
        functions.add("!");

        //Base Conversions init
        baseConversions = new BaseConversions();

        //Shift pairs initializations
        shiftPairs = new HashMap<>();
        shiftPairs.put("Sin", "Cosec");
        shiftPairs.put("Cos", "Sec");
        shiftPairs.put("Tan", "Cot");

        //Logger : Initialization
        logger = LogManager.getLogger(CalculatorUI.class);

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
        btnLog.addActionListener(new FunctionPressListener());

        btnClear.addActionListener(ae -> {
            txtInput.setText("");
            operators.flush();
            leftOperands.flush();
            rightOperands.flush();
            currentLeft = 0;
            currentRight = 0;
        });

        btnEquals.addActionListener(ae -> {
            try {
                if (txtInput.getText().length() > 0 && currentLeft == 0) {
                    rightOperands.push(String.valueOf(eval(txtInput.getText())));
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
                String result = rightOperands.pop();
                String currentInput = txtInput.getText();

                if (ans != null)
                    currentInput = currentInput.replaceAll("ans", ans);

                listHistoryArray.insertElementAt(currentInput.concat("=").concat(result), listHistoryArray.getSize());
                listHistory.setListData(listHistoryArray.toArray());
                txtOutput.setText(result);
                txtInput.setText("");
                ans = result;
                currentLeft = 0;
                currentRight = 0;
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
                logger.info(ex);
            }
        });

        btnMemAdd.addActionListener(ae -> {
            try {
                String numInText = txtInput.getText();
                if (numInText.equals("ans")) {
                    numInText = ans;
                }

                if (mem == null)
                    mem = new Double(numInText);
                else
                    mem += Double.parseDouble(numInText);
            } catch (Exception ex) {
                txtOutput.setText("Syntax error!");
                logger.info(ex);
            }
        });

        btnMemSub.addActionListener(ae -> {
            try {
                String numInText = txtInput.getText();
                if (mem == null)
                    mem = new Double(numInText);
                else
                    mem += Double.parseDouble(numInText);
            } catch (Exception ex) {
                txtOutput.setText("Syntax error!");
                logger.info(ex);
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

        btnAns.addActionListener(e -> {
            if (ans == null) {
                JOptionPane.showMessageDialog(null, "No previous calculations!", "Info", INFORMATION_MESSAGE);
                return;
            }
            txtInput.setText(txtInput.getText().concat("ans"));
        });

        btnShift.addActionListener(e -> {
            // TODO: 12/7/16 Implement shift key behavior
        });

        btnHex.addActionListener(e -> {
            String input = txtInput.getText();
            try {
                baseConversions.toHex(input);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
                logger.info(ex);
            }
        });

        btnOct.addActionListener(e -> {
            String input = txtInput.getText();
            try {
                baseConversions.toOct(input);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
                logger.info(ex);
            }
        });

        btnDec.addActionListener(e -> {
            String input = txtInput.getText();
            try {
                baseConversions.toDec(input);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
                logger.info(ex);
            }
        });

        btnBin.addActionListener(e -> {
            String input = txtInput.getText();
            try {
                baseConversions.toBin(input);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
                logger.info(ex);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logger.error(ex);
        }
        CalculatorUI obj = new CalculatorUI();
        JFrame frame = new JFrame("Calculator");
        frame.setJMenuBar(obj.createMenuBar());
        frame.setSize(new Dimension(542, 357));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(obj.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;
            int current;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean isOperator(int thisChar) {
                while (ch == ' ') nextChar();
                if (ch == thisChar) {
                    nextChar();
                    current = ch;
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length())
                    throw new InvalidNumberException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (isOperator('+'))
                        x += parseTerm(); // addition
                    else if (isOperator('-'))
                        x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (isOperator('*'))
                        x *= parseFactor(); // multiplication

                    else if (isOperator('/'))
                        x /= parseFactor(); // division

                    else if (isOperator('C')) {
                        double r = parseFactor();

                        if (x % 1 > 0)
                            throw new InvalidNumberException(x + " : Not an integer!");

                        if (r % 1 > 0)
                            throw new InvalidNumberException(r + " : Not an integer!");

                        if (x < 0)
                            throw new InvalidNumberException(x + " : Negative integer!");

                        if (r < 0)
                            throw new InvalidNumberException(r + " : Negative integer!");

                        x = Math.combine((long) x, (long) r);
                    } else if (isOperator('P')) {
                        double r = parseFactor();

                        if (x % 1 > 0)
                            throw new InvalidNumberException(x + " : Not an integer!");

                        if (r % 1 > 0)
                            throw new InvalidNumberException(r + " : Not an integer!");

                        if (x < 0)
                            throw new InvalidNumberException(x + " : Negative integer!");

                        if (r < 0)
                            throw new InvalidNumberException(r + " : Negative integer!");

                        x = Math.permute((long) x, (long) r);
                    }

                    else if (isOperator('%'))
                        x = (x * parseFactor()) / 100;

                    else return x;
                }
            }

            double parseFactor() {
                if (isOperator('+'))
                    return parseFactor(); // unary plus
                if (isOperator('-'))
                    return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (isOperator('(')) {
                    // Parse parenthesis
                    x = parseExpression();
                    isOperator(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.' || ch == 8495 || ch == 'π') {
                    // Parse numbers
                    if (ch == 'π')
                        x = java.lang.Math.PI;

                    else if (ch == 8495)
                        x = java.lang.Math.E;

                    else {
                        while ((ch >= '0' && ch <= '9') || ch == '.')
                            nextChar();
                        x = Double.parseDouble(str.substring(startPos, this.pos));
                    }
                } else if ((ch >= 'a' && ch <= 'z') || ch == '√' || ch == '!') {
                    // Parse built-in functions
                    while ((ch >= 'a' && ch <= 'z') || ch == '√' || ch == '!')
                        nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("√"))
                        x = java.lang.Math.sqrt(x);

                    else if (func.equals("sin"))
                        x = java.lang.Math.sin(Math.toRadians(x));

                    else if (func.equals("cos"))
                        x = java.lang.Math.cos(Math.toRadians(x));

                    else if (func.equals("tan"))
                        x = java.lang.Math.tan(Math.toRadians(x));

                    else if (func.equals("!"))
                        x = factorial((long) x);

                    else
                        throw new InvalidNumberException("Unknown function: " + func);

                } else {
                    throw new InvalidNumberException("Unexpected: " + (char) ch);
                }

                if (isOperator('^')) x = java.lang.Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    //Menu bar generation
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menuFile, menuEdit, menuView, subMenu1History;
        JMenuItem itmPreferences, itmLaunchPlotter, itmLoadHistory, itmSaveHistory, itmClearHistory;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //File menu.
        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuFile.getAccessibleContext().setAccessibleDescription("File");
        menuBar.add(menuFile);

        //File menu items
        itmPreferences = new JMenuItem("Preferences");
        itmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
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
        itmSaveHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        subMenu1History.add(itmSaveHistory);

        itmLoadHistory = new JMenuItem("Load...");
        itmLoadHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        subMenu1History.add(itmLoadHistory);

        itmClearHistory = new JMenuItem("Clear");
        itmClearHistory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_MASK));
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

        //Menu item actions
        itmLaunchPlotter.addActionListener(e -> {
            Plotter obj = new Plotter("Plotter");
        });

        itmLoadHistory.addActionListener(e -> history.load(listHistory, listHistoryArray));

        itmSaveHistory.addActionListener(e -> history.save(listHistoryArray));

        itmClearHistory.addActionListener(e -> {
            listHistoryArray.clear();
            listHistory.setListData(listHistoryArray.toArray());
        });

        itmPreferences.addActionListener(e -> {

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
                return ans;
            }

            num = "";
        }
        if (num.equals("ℯ"))
            return String.valueOf(java.lang.Math.E);

        if (num.equals("π"))
            return String.valueOf(java.lang.Math.PI);

        return num;
    }

    private String evaluate() {
        String operator = operators.pop();
        if (!operator.equals("(")) {
            if (functions.contains(operator)) {
                double left = Double.parseDouble(rightOperands.pop());
                switch (operator) {
                    case "Sin":
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
                        //long n = (long) left;
                        if (left % 1 > 0) {
                            throw new InvalidNumberException(left + " : Not an integer!");
                        }
                        rightOperands.push(String.valueOf(factorial((long) left)));
                        break;

                    case "log":
                        rightOperands.push(String.valueOf(logarithm(left)));
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
                        rightOperands.push(String.valueOf(left / right));
                        break;

                    case "%":
                        rightOperands.push(String.valueOf(left * right / 100));
                        break;

                    case "^":
                        rightOperands.push(String.valueOf(power(left, right)));
                        break;

                    case "C":
                    case "P":
                        if (left % 1 > 0) {
                            throw new InvalidNumberException(left + " : Not an integer!");
                        }

                        if (right % 1 > 0) {
                            throw new InvalidNumberException(right + " : Not an integer!");
                        }

                        if (left < 0) {
                            throw new InvalidNumberException(left + " : Negative integer!");
                        }

                        if (right < 0) {
                            throw new InvalidNumberException(right + " : Negative integer!");
                        }

                        if (operator.equals("C"))
                            rightOperands.push(String.valueOf(combine((long) left, (long) right)));

                        else
                            rightOperands.push(String.valueOf(permute((long) left, (long) right)));

                        break;

                    default:
                        throw new InvalidNumberException("Invalid operation!");
                }
            }
        }
        return operator;
    }

    private void traverse(String currentOperator) {
        try {
            if (currentOperator.equals(")")) {

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
                    if (!leftOperands.isEmpty() && operators.isEmpty() && rightOperands.isEmpty())
                        break;

                    if (!operators.isEmpty() && operators.peek().equals("(")) {
                        if (rightOperands.isEmpty()) {
                            rightOperands.push(leftOperands.pop());
                        }

                        if (leftOperands.isEmpty()) {
                            leftOperands.push(rightOperands.pop());
                            break;
                        }
                    }

                    if (leftOperands.isEmpty()) {
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
        } catch (Exception ex) {
            txtOutput.setText("Malformed expression!");
            logger.info(ex);
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
            currentLeft = 0;
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
            if (txtInput.getText().length() > 3 && currentLeft == 0 && currentRight != 0) {
                String unevaluatedPrefix = txtInput.getText().concat(e.getActionCommand()).substring(currentLeft, currentRight);
                leftOperands.push(String.valueOf(eval(unevaluatedPrefix)));
            }
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
            currentLeft = txtInput.getText().length();
        }
    }
}