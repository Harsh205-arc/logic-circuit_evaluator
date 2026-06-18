package io.github.harsh;
import io.github.harsh.ast.Expression;
import java.util.List;

public class App {
    public static void main(String[] args) {
       Tokenizer tokenizer = new Tokenizer();
       List<Token> tokens = tokenizer.getToken("A OR AND D");
       Parser parser = new Parser(tokens);
       Expression expr = parser.parse();
       System.out.println(expr);
    }
}