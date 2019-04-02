package by.epam.third.interpreter;

public class NonterminalExpressionNumber extends AbstractBitExpression {

    private String number;

    public NonterminalExpressionNumber(String number) {
        this.number = number;
    }

    @Override
    public void interpret(Context context) {
        context.pushValue(number);
    }
}
