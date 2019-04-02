package by.epam.third.interpreter;

public class TerminalExpressionNot extends AbstractBitExpression {
    @Override
    public void interpret(Context context) {
        context.pushValue(String.valueOf(~Integer.parseInt(context.popValue())));
    }
}
