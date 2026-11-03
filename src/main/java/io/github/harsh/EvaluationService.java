
package io.github.harsh;

import io.github.harsh.ast.Expression;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    public List<TruthTableRow> evaluate(String expression) {

        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException(
                "Expression cannot be empty."
            );
        }

        try {
            Tokenizer tokenizer = new Tokenizer();
            List<Token> tokens = tokenizer.getToken(expression);

            Parser parser = new Parser(tokens);
            Expression parsedExpression = parser.parse();

            return TruthTableGenerator.generate(parsedExpression);

        } 
        catch (RuntimeException exception) {
            throw new IllegalArgumentException(
                "Invalid Boolean expression: " + exception.getMessage(),
                exception
            );
        }
    }
}
