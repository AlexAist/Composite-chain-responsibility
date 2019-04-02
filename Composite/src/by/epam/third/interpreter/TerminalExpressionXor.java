package by.epam.third.interpreter;

public class TerminalExpressionXor extends AbstractBitExpression {
    @Override
    public void interpret(Context context) {
        context.pushValue(String.valueOf(Integer.parseInt(context.popValue()) ^ Integer.parseInt(context.popValue())));
    }
}
