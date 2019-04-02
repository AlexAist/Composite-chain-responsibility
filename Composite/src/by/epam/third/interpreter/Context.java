package by.epam.third.interpreter;

import java.util.ArrayDeque;
import java.util.Stack;

public class Context{

    private Stack<String> contextValues = new Stack<>();

    public String popValue(){
        return contextValues.pop();
    }

    public void pushValue(String value){
        this.contextValues.push(value);
    }
}