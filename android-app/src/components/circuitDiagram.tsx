import React from 'react';

import {
    View,
    Text,
    StyleSheet,
    ScrollView,
} from 'react-native';

import Svg, {
    Circle,
    Line,
    Text as SvgText,
} from 'react-native-svg';

import type {
    CircuitDiagram as CircuitDiagramData,
    CircuitNode,
} from '../../services/circuitService';

import {
    calculateCircuitLayout,
} from './circuit/circuitLayout';

import CircuitWire from './circuit/circuitWire';

import GateSymbol, {
    GATE_INPUT_X,
    GATE_INPUT_OFFSETS,
} from './circuit/gateSymbol';

interface CircuitDiagramProps {
    diagram: CircuitDiagramData;
}

const INPUT_RADIUS = 20;

export default function CircuitDiagram({
    diagram,
}: CircuitDiagramProps) {

    if (!diagram || diagram.nodes.length === 0) {
        return (
            <View style={styles.container}>
                <Text style={styles.emptyText}>
                    No circuit data available.
                </Text>
            </View>
        );
    }

    const { positions, width, height } =
        calculateCircuitLayout(diagram);

    const nodesById = new Map<string, CircuitNode>(
        diagram.nodes.map((node) => [node.id, node])
    );

    const getOutputX = (node: CircuitNode, x: number) => {
        if (node.type === 'INPUT') {
            return x + INPUT_RADIUS;
        }

        if (node.type === 'NAND' || node.type === 'XNOR') {
            return x + 66;
        }

        if (node.type === 'NOT') {
            return x + 53;
        }

        return x + 55;
    };

    const outputNode = nodesById.get(diagram.output);
    const outputPosition = positions[diagram.output];

    const outputX = outputNode && outputPosition
        ? getOutputX(outputNode, outputPosition.x)
        : 0;

    const canvasWidth = Math.max(width, outputX + 110);

    return (
        <View style={styles.container}>

            <View style={styles.header}>
                <Text style={styles.heading}>
                    Logic Circuit
                </Text>

                <Text style={styles.description}>
                    Boolean expression circuit representation
                </Text>
            </View>

            <ScrollView
                horizontal
                showsHorizontalScrollIndicator
            >
                <View
                    style={{
                        width: canvasWidth,
                        height,
                    }}
                >

                    {/* Wires and input terminals */}
                    <Svg
                        width={canvasWidth}
                        height={height}
                        style={StyleSheet.absoluteFill}
                    >

                        {/* Draw every connection */}
                        {diagram.connections.map(
                            (connection, index) => {

                                const sourceNode =
                                    nodesById.get(connection.source);

                                const targetNode =
                                    nodesById.get(connection.target);

                                const sourcePosition =
                                    positions[connection.source];

                                const targetPosition =
                                    positions[connection.target];

                                if (
                                    !sourceNode ||
                                    !targetNode ||
                                    !sourcePosition ||
                                    !targetPosition
                                ) {
                                    return null;
                                }

                                const inputIndex =
                                    targetNode.inputs.indexOf(
                                        connection.source
                                    );

                                const targetOffset =
                                    targetNode.type === 'NOT'
                                        ? 0
                                        : GATE_INPUT_OFFSETS[
                                            inputIndex === 0 ? 0 : 1
                                        ];

                                const startX = getOutputX(
                                    sourceNode,
                                    sourcePosition.x
                                );

                                const endX =
                                    targetPosition.x + GATE_INPUT_X;

                                return (
                                    <CircuitWire
                                        key={`wire-${index}`}
                                        x1={startX}
                                        y1={sourcePosition.y}
                                        x2={endX}
                                        y2={
                                            targetPosition.y +
                                            targetOffset
                                        }
                                    />
                                );
                            }
                        )}

                        {/* Draw input terminals */}
                        {diagram.nodes
                            .filter((node) => node.type === 'INPUT')
                            .map((node) => {

                                const position = positions[node.id];

                                return (
                                    <React.Fragment key={node.id}>

                                        <Circle
                                            cx={position.x}
                                            cy={position.y}
                                            r={INPUT_RADIUS}
                                            fill="#1D4ED8"
                                            stroke="#60A5FA"
                                            strokeWidth={2}
                                        />

                                        <SvgText
                                            x={position.x}
                                            y={position.y + 5}
                                            fill="#FFFFFF"
                                            fontSize={15}
                                            fontWeight="bold"
                                            textAnchor="middle"
                                        >
                                            {node.label}
                                        </SvgText>

                                    </React.Fragment>
                                );
                            })}

                        {/* Final output wire and terminal */}
                        {outputNode && outputPosition && (
                            <>
                                <Line
                                    x1={outputX}
                                    y1={outputPosition.y}
                                    x2={outputX + 35}
                                    y2={outputPosition.y}
                                    stroke="#4ADE80"
                                    strokeWidth={2.5}
                                />

                                <Circle
                                    cx={outputX + 39}
                                    cy={outputPosition.y}
                                    r={4}
                                    fill="#4ADE80"
                                />

                                <SvgText
                                    x={outputX + 49}
                                    y={outputPosition.y + 5}
                                    fill="#4ADE80"
                                    fontSize={13}
                                    fontWeight="bold"
                                >
                                    OUTPUT
                                </SvgText>
                            </>
                        )}

                    </Svg>

                    {/* Draw actual gate symbols above the wires */}
                    {diagram.nodes
                        .filter((node) => node.type !== 'INPUT')
                        .map((node) => {

                            const position = positions[node.id];

                            return (
                                <GateSymbol
                                    key={node.id}
                                    type={node.type}
                                    x={position.x}
                                    y={position.y}
                                />
                            );
                        })}

                </View>
            </ScrollView>

            <Text style={styles.outputInfo}>
                Output node: {diagram.output}
            </Text>

        </View>
    );
}

const styles = StyleSheet.create({

    container: {
        backgroundColor: '#1E293B',
        padding: 18,
        borderRadius: 15,
        marginBottom: 25,
        overflow: 'hidden',
    },

    header: {
        marginBottom: 25,
    },

    heading: {
        color: '#FFFFFF',
        fontSize: 19,
        fontWeight: 'bold',
        marginBottom: 7,
    },

    description: {
        color: '#94A3B8',
        fontSize: 13,
    },

    emptyText: {
        color: '#94A3B8',
        fontSize: 14,
    },

    outputInfo: {
        color: '#4ADE80',
        fontSize: 12,
        marginTop: 15,
        fontWeight: '600',
    },

});