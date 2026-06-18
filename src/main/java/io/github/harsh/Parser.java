package io.github.harsh;
import io.github.harsh.ast.*;

import java.util.*;
public class Parser {
    //initialisation
    private final List<Token> tokens;
    private int current =0;
    //constructor
    public Parser(List<Token> token){
        this.tokens=token;
    }
    // utility funcutions
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
    private Token consume(TokenType type,String message){
        if(check(type)){
            return advance();
        }
        throw new RuntimeException (message);
    }
    //paser starts
    private Expression primary(){
        if(match(TokenType.IDENTIFIER)){
            return new VariableExpression(previous().getValue());
        }
        if(match(TokenType.LPAREN)){
            Expression expr=or();
            consume(TokenType.RPAREN, "Expected ')' before expresion" );
            return expr;
        }
        throw new RuntimeException("Invalid input");
    }
    private Expression not(){
        if(match(TokenType.NOT)){
            Token op=previous();
            return new UnaryExpression(op.getTokenType(),not());
        }
        return primary();
    }
    private Expression and() {
        Expression expr = not();
        while(match(TokenType.AND) || match(TokenType.NAND)) {
            Token operator = previous();
            Expression right = not();
            expr = new BinaryExpression(
                expr,
                operator.getTokenType(),
                right
            );
        }
        return expr;
    }
    private Expression xor(){
        Expression expr= and();
        while(match(TokenType.XOR)|| match(TokenType.XNOR)){
            Token operator= previous();
            Expression right= and();
            expr=new BinaryExpression(expr, operator.getTokenType(), right);
        }
        return expr;
    }
    private Expression or() {
        Expression expr = xor();
        while(match(TokenType.OR)) {
            Token operator = previous();
            Expression right = xor();
            expr = new BinaryExpression(expr,operator.getTokenType(),right);
        }
        return expr;
    }    
    public Expression parse() {
        Expression expr= or();
        if(!isAtEnd()){
            throw new RuntimeException("Unexpected token '"+peek().getValue()+"'");
        }
        return expr;
    }
}