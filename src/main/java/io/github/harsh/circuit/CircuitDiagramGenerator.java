
package io.github.harsh.circuit;

import io.github.harsh.ast.*;
import java.util.*;

public class CircuitDiagramGenerator {

    // Stores all the generated circuit nodes
    private final List<CircuitNode> nodes = new ArrayList<>();

    // Stores all the connections between nodes
    private final List<CircuitConnection> connections = new ArrayList<>();

    // Maps variable names to their input node IDs.
    // This prevents duplicate input nodes for repeated variables.
    private final Map<String, String> inputNodes = new LinkedHashMap<>();

    // Counters for generating unique IDs
    private int gateCounter = 1;
    private int inputCounter = 1;


    // Main method called to generate the circuit
    public static CircuitDiagram generate(Expression expression) {

        if (expression == null) {
            throw new IllegalArgumentException(
                "Expression cannot be null."
            );
        }

        CircuitDiagramGenerator generator =
            new CircuitDiagramGenerator();

        // Recursively generate the circuit
        String outputId = generator.generateNode(expression);

        // Return the complete circuit
        return new CircuitDiagram(
            generator.nodes,
            generator.connections,
            outputId
        );
    }


    // Recursively traverses the AST
    private String generateNode(Expression expression) {

        // CASE 1: Variable expression (A, B, C...)
        if (expression instanceof VariableExpression) {

            VariableExpression variable =
                (VariableExpression) expression;

            String variableName = variable.getVar();

            // Check whether this variable already has an input node
            if (inputNodes.containsKey(variableName)) {
                return inputNodes.get(variableName);
            }

            // Generate a unique input ID
            String inputId = "input_" + inputCounter++;

            CircuitNode inputNode = new CircuitNode(
                inputId,
                "INPUT",
                variableName,
                Collections.emptyList()
            );

            nodes.add(inputNode);

            // Store the variable and its corresponding node ID
            inputNodes.put(variableName, inputId);

            return inputId;
        }


        // CASE 2: Unary expression (NOT)
        if (expression instanceof UnaryExpression) {

            UnaryExpression unary =
                (UnaryExpression) expression;

            // Generate the input of the NOT gate first
            String inputId =
                generateNode(unary.getExpression());

            // Generate the gate
            String gateId = "gate_" + gateCounter++;

            String gateType = unary.getOperator().name();

            CircuitNode gate = new CircuitNode(
                gateId,
                gateType,
                gateType,
                Collections.singletonList(inputId)
            );

            nodes.add(gate);

            // Connect the input node to the NOT gate
            connections.add(
                new CircuitConnection(inputId, gateId)
            );

            return gateId;
        }


        // CASE 3: Binary expression (AND, OR, XOR, etc.)
        if (expression instanceof BinaryExpression) {

            BinaryExpression binary =
                (BinaryExpression) expression;

            // Recursively generate the left subtree
            String leftId =
                generateNode(binary.getLeft());

            // Recursively generate the right subtree
            String rightId =
                generateNode(binary.getRight());

            // Generate a unique gate ID
            String gateId = "gate_" + gateCounter++;

            String gateType = binary.getOperator().name();

            // Create the gate with its two input IDs
            CircuitNode gate = new CircuitNode(
                gateId,
                gateType,
                gateType,
                Arrays.asList(leftId, rightId)
            );

            nodes.add(gate);

            // Connect left input to the gate
            connections.add(
                new CircuitConnection(leftId, gateId)
            );

            // Connect right input to the gate
            connections.add(
                new CircuitConnection(rightId, gateId)
            );

            return gateId;
        }


        // Unsupported AST node
        throw new IllegalArgumentException(
            "Unsupported expression type: "
            + expression.getClass().getName()
        );
    }
}
