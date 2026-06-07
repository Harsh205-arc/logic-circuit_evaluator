package io.github.harsh;

import java.util.List;

public class App {
    public static void main(String[] args) {

        Tokenizer tokenizer = new Tokenizer();

        List<Token> tokens = tokenizer.getToken("A XOR B XNOR C");

        for (Token token : tokens) {
            System.out.println(
                token.getTokenType() + " -> " + token.getValue()
            );
        }
    }
}