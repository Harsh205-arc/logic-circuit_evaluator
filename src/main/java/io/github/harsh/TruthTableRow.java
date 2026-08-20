package io.github.harsh;
import java.util.Map;

public class TruthTableRow {

    private final Map<String, Boolean> variables;
    private final boolean result;

    public TruthTableRow(Map<String, Boolean> variables, boolean result) {
        this.variables = variables;
        this.result = result;
    }

    public Map<String, Boolean> getVariables() {
        return variables;
    }

    public boolean getResult() {
        return result;
    }
}