package io.github.harsh.circuit;

import java.util.List;

public class CircuitDiagram {

    private List<CircuitNode> nodes;
    private List<CircuitConnection> connections;
    private String output;

    public CircuitDiagram(
            List<CircuitNode> nodes,
            List<CircuitConnection> connections,
            String output) {

        this.nodes = nodes;
        this.connections = connections;
        this.output = output;
    }

    public List<CircuitNode> getNodes() {
        return nodes;
    }

    public List<CircuitConnection> getConnections() {
        return connections;
    }

    public String getOutput() {
        return output;
    }
}