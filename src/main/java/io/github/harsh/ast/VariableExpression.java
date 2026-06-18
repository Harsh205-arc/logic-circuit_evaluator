package io.github.harsh.ast;

public class VariableExpression extends Expression {
    private final String name;
    public VariableExpression(String n){
        name=n;
    }
    @Override
    public String toString() {
        return name;
    }
}
