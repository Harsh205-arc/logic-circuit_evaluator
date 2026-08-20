package io.github.harsh;
import io.github.harsh.ast.*;
import java.util.Map;

public class Evaluator {

    public boolean evaluate(Expression expr,Map<String, Boolean> variables) {
        // expr is the result from the parser and variables is the map keeping track of every token

        if (expr instanceof VariableExpression) {
            VariableExpression var = (VariableExpression) expr;

            if (!variables.containsKey(var.getVar())) {
                throw new RuntimeException("No value provided for variable: "+ var.getVar());
            }

            return variables.get(var.getVar());
        }

        if (expr instanceof UnaryExpression) {
            UnaryExpression unary = (UnaryExpression) expr;
 
            boolean value =evaluate(unary.getExpression(), variables);

            switch (unary.getOperator()) {
                case NOT:
                    return !value;

                default:
                    throw new RuntimeException("Unsupported unary operator: "+ unary.getOperator());
            }
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression binary = (BinaryExpression) expr;

            boolean left =evaluate(binary.getLeft(), variables);
            boolean right =evaluate(binary.getRight(), variables);
            switch (binary.getOperator()) {

                case AND:
                    return left && right;

                case OR:
                    return left || right;

                case XOR:
                    return left != right;

                case NAND:
                    return !(left && right);

                case XNOR:
                    return left == right;

                default:
                    throw new RuntimeException("Unsupported binary operator: "+ binary.getOperator());
            }
        }

        throw new RuntimeException("Unknown expression type: "+ expr.getClass().getSimpleName());
    }
}