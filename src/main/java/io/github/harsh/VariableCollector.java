package io.github.harsh;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import io.github.harsh.ast.*;

public class VariableCollector {

    public static Set<String> collectVariables(Expression expression) {
        Set<String> variables = new HashSet<>();
        collectRecursive(expression, variables);
        return variables;
    }

    private static void collectRecursive(Expression expression,Set<String> variables) {

        if (expression instanceof VariableExpression variable) {

            variables.add(variable.getVar());

        } else if (expression instanceof UnaryExpression unary) {

            collectRecursive(unary.getExpression(), variables);

        } else if (expression instanceof BinaryExpression binary) {

            collectRecursive(binary.getLeft(), variables);
            collectRecursive(binary.getRight(), variables);
        }
    }

    public static Map<String, Boolean> getVariableValues(Set<String> variables) {

        Scanner scanner = new Scanner(System.in);

        Map<String, Boolean> values = new HashMap<>();

        for (String variable : variables) {

            boolean value = readBoolean(scanner, variable);

            values.put(variable, value);
        }

        return values;
    }

    private static boolean readBoolean(Scanner scanner, String variable) {

        while (true) {

            System.out.print("Enter value for " + variable + " (true/false): ");

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("true")) {
                return true;
            }

            if (input.equals("false")) {
                return false;
            }

            System.out.println("Invalid input. Please enter 'true' or 'false'.");
        }
    }
}
