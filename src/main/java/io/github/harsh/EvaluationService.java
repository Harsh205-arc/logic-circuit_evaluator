
package io.github.harsh;

import io.github.harsh.ast.Expression;
import io.github.harsh.circuit.CircuitDiagram;
import io.github.harsh.circuit.CircuitDiagramGenerator;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    // Existing truth table generation
    public List<TruthTableRow> evaluate(String expression) {

        Expression parsedExpression = parseExpression(expression);

        return TruthTableGenerator.generate(parsedExpression);
    }

    // New circuit diagram generation
    public CircuitDiagram generateCircuit(String expression) {

        Expression parsedExpression = parseExpression(expression);

        return CircuitDiagramGenerator.generate(parsedExpression);
    }

    // Shared parsing logic for both operations
    private Expression parseExpression(String expression) {

        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException(
                "Expression cannot be empty."
            );
        }

        try {

            Tokenizer tokenizer = new Tokenizer();

            List<Token> tokens = tokenizer.getToken(expression);

            Parser parser = new Parser(tokens);

            return parser.parse();

        } catch (RuntimeException exception) {

            throw new IllegalArgumentException(
                "Invalid Boolean expression: " + exception.getMessage(),
                exception
            );
        }
    }
}
