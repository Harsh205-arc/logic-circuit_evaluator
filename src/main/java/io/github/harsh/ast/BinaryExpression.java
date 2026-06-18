package io.github.harsh.ast;

import io.github.harsh.TokenType;

public class BinaryExpression extends Expression {
    private Expression left;
    private TokenType oprator;
    private Expression right;
    public BinaryExpression(Expression l,TokenType op,Expression r){
        this.left=l;
        this.oprator=op;
        this.right=r;
    }
    @Override
    public String toString() {
        return "(" + left + " " + oprator + " " + right + ")";
    }
}
