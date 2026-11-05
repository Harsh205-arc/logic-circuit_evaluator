import type { CircuitDiagram, CircuitNode } from '../../../services/circuitService';

export interface NodePosition {
    x: number;
    y: number;
    depth: number;
}

export interface CircuitLayoutResult {
    positions: Record<string, NodePosition>;
    width: number;
    height: number;
}

const LEFT_PADDING = 75;
const TOP_PADDING = 75;

const COLUMN_GAP = 190;
const ROW_GAP = 105;

const MIN_WIDTH = 450;
const MIN_HEIGHT = 220;

export function calculateCircuitLayout(
    diagram: CircuitDiagram
): CircuitLayoutResult {

    const nodesById = new Map<string, CircuitNode>(
        diagram.nodes.map(node => [node.id, node])
    );

    const positions: Record<string, NodePosition> = {};
    const depths: Record<string, number> = {};

    let nextInputY = TOP_PADDING;

    // Recursively calculate each node's position.
    function calculatePosition(nodeId: string): NodePosition {

        // Reuse an already calculated position.
        if (positions[nodeId]) {
            return positions[nodeId];
        }

        const node = nodesById.get(nodeId);

        if (!node) {
            throw new Error(`Unknown circuit node: ${nodeId}`);
        }

        // Input variables are placed vertically.
        if (node.type === 'INPUT') {

            const position: NodePosition = {
                x: LEFT_PADDING,
                y: nextInputY,
                depth: 0,
            };

            nextInputY += ROW_GAP;

            positions[nodeId] = position;
            depths[nodeId] = 0;

            return position;
        }

        if (node.inputs.length === 0) {
            throw new Error(`Gate ${nodeId} has no inputs.`);
        }

        // Calculate positions of all child nodes first.
        const inputPositions = node.inputs.map(
            inputId => calculatePosition(inputId)
        );

        // Determine the gate's horizontal depth.
        const depth = Math.max(
            ...inputPositions.map(position => position.depth)
        ) + 1;

        // Centre the gate between its input nodes.
        const averageY =
            inputPositions.reduce(
                (sum, position) => sum + position.y,
                0
            ) / inputPositions.length;

        const position: NodePosition = {
            x: LEFT_PADDING + depth * COLUMN_GAP,
            y: averageY,
            depth,
        };

        positions[nodeId] = position;
        depths[nodeId] = depth;

        return position;
    }

    // Start from the final output and recursively position
    // everything that contributes to it.
    calculatePosition(diagram.output);

    // Calculate canvas dimensions.
    const maxDepth = Math.max(
        ...Object.values(depths),
        0
    );

    const maxY = Math.max(
        ...Object.values(positions).map(position => position.y),
        TOP_PADDING
    );

    const width = Math.max(
        MIN_WIDTH,
        LEFT_PADDING + maxDepth * COLUMN_GAP + 180
    );

    const height = Math.max(
        MIN_HEIGHT,
        maxY + ROW_GAP
    );

    return {
        positions,
        width,
        height,
    };
}