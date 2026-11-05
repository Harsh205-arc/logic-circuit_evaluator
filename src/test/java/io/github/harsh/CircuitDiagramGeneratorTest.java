
package io.github.harsh.circuit;

import io.github.harsh.Parser;
import io.github.harsh.Token;
import io.github.harsh.Tokenizer;
import io.github.harsh.ast.Expression;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CircuitDiagramGeneratorTest {

    // Helper method: converts a Boolean expression into an AST
    private Expression parseExpression(String expression) {

        Tokenizer tokenizer = new Tokenizer();

        List<Token> tokens = tokenizer.getToken(expression);

        Parser parser = new Parser(tokens);

        return parser.parse();
    }

    @Test
    void testBasicCircuitGeneration() {

        // Given Boolean expression
        String expression = "(A AND B) OR C";

        // Step 1: Parse expression into AST
        Expression ast = parseExpression(expression);

        // Step 2: Generate circuit
        CircuitDiagram diagram =
                CircuitDiagramGenerator.generate(ast);

        // Step 3: Verify nodes
        List<CircuitNode> nodes = diagram.getNodes();

        assertEquals(5, nodes.size());

        // Verify input A
        assertEquals("INPUT", nodes.get(0).getType());
        assertEquals("A", nodes.get(0).getLabel());

        // Verify input B
        assertEquals("INPUT", nodes.get(1).getType());
        assertEquals("B", nodes.get(1).getLabel());

        // Verify AND gate
        assertEquals("AND", nodes.get(2).getType());
        assertEquals(2, nodes.get(2).getInputs().size());

        // Verify input C
        assertEquals("INPUT", nodes.get(3).getType());
        assertEquals("C", nodes.get(3).getLabel());

        // Verify OR gate
        assertEquals("OR", nodes.get(4).getType());
        assertEquals(2, nodes.get(4).getInputs().size());

        // Step 4: Verify connections
        List<CircuitConnection> connections =
                diagram.getConnections();

        assertEquals(4, connections.size());

        // Step 5: Verify final output
        assertEquals("gate_2", diagram.getOutput());
    }

    @Test
    void testNotGateGeneration() {

        Expression ast = parseExpression("NOT A");

        CircuitDiagram diagram =
                CircuitDiagramGenerator.generate(ast);

        assertEquals(2, diagram.getNodes().size());

        CircuitNode notGate = diagram.getNodes().get(1);

        assertEquals("NOT", notGate.getType());
        assertEquals(1, notGate.getInputs().size());

        assertEquals("gate_1", diagram.getOutput());

        assertEquals(1, diagram.getConnections().size());
    }

    @Test
    void testRepeatedVariable() {

        Expression ast = parseExpression("A AND A");

        CircuitDiagram diagram =
                CircuitDiagramGenerator.generate(ast);

        // A should only have one input node
        long inputCount = diagram.getNodes()
                .stream()
                .filter(node -> node.getType().equals("INPUT"))
                .count();

        assertEquals(1, inputCount);

        // Both gate inputs should reference the same node
        CircuitNode gate = diagram.getNodes().get(1);

        assertEquals(
                gate.getInputs().get(0),
                gate.getInputs().get(1)
        );
    }

    @Test
    void testNandXorAndXnorGeneration() {

        String[] operators = {"NAND", "XOR", "XNOR"};

        for (String operator : operators) {

            Expression ast = parseExpression(
                    "A " + operator + " B"
            );

            CircuitDiagram diagram =
                    CircuitDiagramGenerator.generate(ast);

            CircuitNode gate = diagram.getNodes().get(2);

            assertEquals(operator, gate.getType());

            assertEquals(2, gate.getInputs().size());
        }
    }
}
