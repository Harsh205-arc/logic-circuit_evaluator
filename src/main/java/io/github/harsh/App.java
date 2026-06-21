package io.github.harsh;
import io.github.harsh.ast.Expression;
import java.util.*;
public class App {
    public static void main(String[] args) {

        Tokenizer tokenizer = new Tokenizer();

        List<Token> tokens =tokenizer.getToken("NOT ((A XOR B) NAND (C XNOR D)) OR (E AND (F OR (G NAND H)))");
        Parser parser = new Parser(tokens);
        Expression expr = parser.parse();
        System.out.println("AST: " + expr);
        Map<String, Boolean> variables =new HashMap<>();

        variables.put("A", true);
        variables.put("B", false);
        variables.put("C",true);
        variables.put("D",true);
        variables.put("E", false);
        variables.put("F", true);
        variables.put("G",true);
        variables.put("H",false);
        Evaluator evaluator = new Evaluator();
        boolean result =evaluator.evaluate(expr, variables);
        System.out.println("Result: " + result);
    }
}