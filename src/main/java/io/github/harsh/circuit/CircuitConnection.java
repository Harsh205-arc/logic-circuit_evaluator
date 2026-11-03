package io.github.harsh.circuit;

public class CircuitConnection {

    private String source;
    private String target;

    public CircuitConnection(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}