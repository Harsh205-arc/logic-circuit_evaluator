package io.github.harsh.ast;

import io.github.harsh.TokenType;
// class which will handle NOT operations
public class UnaryExpression extends Expression {
    private TokenType oprator;
    private Expression expression;
    public UnaryExpression(TokenType type,Expression exp){
        this.oprator=type;
        this.expression=exp;
    }
    public TokenType getOperator(){
        return oprator;
    }
    public Expression getExpression(){
        return expression;
    }
    @Override 
    public String toString(){
        return oprator+" "+ expression;
    }
}
