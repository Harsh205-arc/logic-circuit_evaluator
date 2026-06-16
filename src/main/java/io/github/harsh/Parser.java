package io.github.harsh;
import io.github.harsh.ast.Expression;
import io.github.harsh.ast.VariableExpression;

import java.util.*;
public class Parser {
    private final List<Token> tokens;
    private int current =0;
    public Parser(List<Token> token){
        this.tokens=token;
    }
    private Token peek(){
        return tokens.get(current);
    }
    private Token previous(){
        return tokens.get(current-1);
    }
    private boolean isAtEnd(){
        return peek().getTokenType() == TokenType.EOF;
    }
    private Token advance(){
        if(!isAtEnd()){
            current++;
        }
        return previous();
    }
    private boolean check(TokenType type){
    return peek().getTokenType() == type;
    }
    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private Expression primary(){
        if(match(TokenType.IDENTIFIER)){
            return new VariableExpression(previous().getValue());
        }
        throw new RuntimeException("Expected Variable");
    }
    public Expression parse() {
        return primary();
    }
}