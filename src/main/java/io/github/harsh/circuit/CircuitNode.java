package io.github.harsh.circuit;

import java.util.List;

public class CircuitNode {

    private String id;
    private String type;
    private String label;
    private List<String> inputs;

    public CircuitNode(String id, String type, String label, List<String> inputs) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.inputs = inputs;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getInputs() {
        return inputs;
    }
}
