
const API_BASE_URL = 'http://localhost:8080';

// Represents an individual input or logic gate
export interface CircuitNode {
    id: string;
    type: 'INPUT' | 'AND' | 'OR' | 'NOT' | 'XOR' | 'NAND' | 'XNOR';
    label: string;
    inputs: string[];
}

// Represents a wire connecting two nodes
export interface CircuitConnection {
    source: string;
    target: string;
}

// Represents the complete circuit returned by Spring Boot
export interface CircuitDiagram {
    nodes: CircuitNode[];
    connections: CircuitConnection[];
    output: string;
}

// Sends the expression to the backend and returns the circuit
export async function generateCircuit(
    expression: string
): Promise<CircuitDiagram> {

    if (!expression.trim()) {
        throw new Error('Expression cannot be empty.');
    }

    const url = `${API_BASE_URL}/api/circuit`;

    let response: Response;

    try {
        response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                expression: expression,
            }),
        });
    } catch (error) {
        throw new Error(
            `Cannot reach backend at ${url}. ${
                error instanceof Error ? error.message : 'Network error'
            }`
        );
    }

    const responseText = await response.text();

    let data: any;

    try {
        data = responseText ? JSON.parse(responseText) : null;
    } catch {
        throw new Error('Backend returned an invalid JSON response.');
    }

    if (!response.ok) {
        throw new Error(
            data?.error ?? `Server returned status ${response.status}`
        );
    }

    if (
        !data ||
        !Array.isArray(data.nodes) ||
        !Array.isArray(data.connections) ||
        typeof data.output !== 'string'
    ) {
        throw new Error('Backend returned an invalid circuit structure.');
    }

    return data as CircuitDiagram;
}
