package io.github.harsh;

import io.github.harsh.ast.Expression;

import java.util.List;
import java.util.Set;

public class App {

    public static void main(String[] args) {

        Tokenizer tokenizer = new Tokenizer();

        String input = "A AND B ";

        // Tokenize
        var tokens = tokenizer.getToken(input);

        // Parse
        Parser parser = new Parser(tokens);
        Expression expr = parser.parse();

        System.out.println("AST: " + expr);

        // Collect variables
        Set<String> variables = VariableCollector.collectVariables(expr);

        System.out.println("Variables: " + variables);

        // Generate truth table
        List<TruthTableRow> table =
                TruthTableGenerator.generate(expr);

        // Print truth table
        for (TruthTableRow row : table) {
            System.out.println(
                    row.getVariables() + " -> " + row.getResult()
            );
        }
    }
}