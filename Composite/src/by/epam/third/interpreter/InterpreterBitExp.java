package by.epam.third.interpreter;

import by.epam.third.exception.BasicException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterpreterBitExp {

    private List<AbstractBitExpression> listExpression = new ArrayList<>();
    private final String REGEX_NUMBER;
    private final String REGEX_SPACE;
    private final String REGEX_POLISH_CONVERT;

    public InterpreterBitExp() {
        REGEX_POLISH_CONVERT = "^(\\d+|\\||&|\\(|\\)|\\^|~|<<<|>>>|<<|>>)";
        REGEX_SPACE = " ";
        REGEX_NUMBER = "[0-9]+";
    }

    private Map<String, Integer> initialize() {
        Map<String, Integer> priority = new HashMap<>();
        priority.put("|", 10);//10
        priority.put("&", 8);//8
        priority.put("~", 2);//2
        priority.put("^", 9);//9
        priority.put(">>", 5);//5
        priority.put("<<", 5);//5
        priority.put(">>>", 5);//5
        return priority;
    }

    public String convertToPolishNotation(final String expression) {
        Map<String, Integer> priority = initialize();
        String result = "";
        String newExpression = expression;
        final Stack<String> stack = new Stack<>();
        final Pattern pattern = Pattern.compile(REGEX_POLISH_CONVERT);
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String part = matcher.group();
            if (part.matches(REGEX_NUMBER)) {
                result = String.join(REGEX_SPACE, result, part);
            } else {
                if (part.equals("~") || part.equals("(")) {
                    stack.push(part);
                } else {
                    if (part.equals(")")) {
                        while (true) {
                            part = stack.pop();
                            if (part.equals("(")) {
                                break;
                            }
                            result = String.join(REGEX_SPACE, result, part);
                        }
                    } else {
                        String operation;
                        while (!stack.isEmpty()) {
                            operation = stack.pop();
                            if (operation.equals("(") || priority.get(operation) > priority.get(part)) {
                                stack.push(operation);
                                break;
                            }
                            result = String.join(REGEX_SPACE, result, operation);
                        }
                        stack.push(part);
                    }
                }
            }
            newExpression = newExpression.substring(part.length());
            matcher = pattern.matcher(newExpression);
        }
        while (!stack.isEmpty()) {
            result = String.join(" ", result, stack.pop());
        }
        return result.trim();
    }

    private void calculateBitExpression(final String expression) throws BasicException {
        final List<String> list = new ArrayList<>(Arrays.asList(expression.split(REGEX_SPACE)));
        for (final String symbol : list) {
            if (symbol.matches(REGEX_NUMBER)) {
                this.listExpression.add(new NonterminalExpressionNumber(symbol));
            } else {
                switch (symbol) {
                    case "~":{
                        listExpression.add(new TerminalExpressionNot());
                        break;
                    }
                    case "&": {
                        listExpression.add(new TerminalExpressionAnd());
                        break;
                    }
                    case "|": {
                        listExpression.add(new TerminalExpressionOr());
                        break;
                    }
                    case "^": {
                        listExpression.add(new TerminalExpressionXor());
                        break;
                    }
                    case ">>": {
                        listExpression.add(new TerminalExpressionRightShift());
                        break;
                    }
                    case "<<": {
                        listExpression.add(new TerminalExpressionLeftShift());
                        break;
                    }
                    case ">>>": {
                        listExpression.add(new TerminalExpressionTripleRightShift());
                        break;
                    }
                    default: {
                        throw new BasicException("Invalid type");
                    }
                }
            }
        }
    }

    public String result(String expression) throws BasicException {
        calculateBitExpression(expression);
        Context context = new Context();
        for (AbstractBitExpression terminal : listExpression) {
            terminal.interpret(context);
        }
        return context.popValue();
    }
}
