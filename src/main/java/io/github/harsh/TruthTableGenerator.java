package io.github.harsh;

import io.github.harsh.ast.Expression;

import java.util.*;

public class TruthTableGenerator {

    public static List<TruthTableRow> generate(Expression expression) {

        Set<String> variableSet = VariableCollector.collectVariables(expression);

        List<String> variables = new ArrayList<>(variableSet);

        int numberOfRows = 1 << variables.size();

        List<TruthTableRow> table = new ArrayList<>();

        Evaluator evaluator = new Evaluator();

        for (int row = 0; row < numberOfRows; row++) {

            Map<String, Boolean> values = new LinkedHashMap<>();

            for (int i = 0; i < variables.size(); i++) {

                boolean value = (row & (1 << (variables.size() - 1 - i))) != 0;

                values.put(variables.get(i), value);
            }

            boolean result = evaluator.evaluate(expression, values);

            table.add(new TruthTableRow(values, result));
        }

        return table;
    }
}