import React from 'react';

import Svg, {
    Path,
    Circle,
    Text as SvgText,
} from 'react-native-svg';

interface GateSymbolProps {
    type: string;
    x: number;
    y: number;
}

export const GATE_WIDTH = 90;
export const GATE_HEIGHT = 70;

export const GATE_INPUT_X = -37;
export const GATE_OUTPUT_X = 55;

export const GATE_INPUT_OFFSETS = [-17, 17];

export default function GateSymbol({
    type,
    x,
    y,
}: GateSymbolProps) {

    const bodyLeft = 8;
    const bodyRight = 98;
    const middle = 35;

    const isXor = type === 'XOR' || type === 'XNOR';
    const isInverting =
        type === 'NOT' || type === 'NAND' || type === 'XNOR';

    let gatePath = '';

    switch (type) {
        case 'AND':
        case 'NAND':
            gatePath = `
                M ${bodyLeft} 0
                L 53 0
                A 35 35 0 0 1 53 70
                L ${bodyLeft} 70
                Z
            `;
            break;

        case 'OR':
        case 'XOR':
        case 'XNOR':
            gatePath = `
                M ${bodyLeft} 0
                Q 28 35 ${bodyLeft} 70
                Q 63 70 ${bodyRight} 35
                Q 63 0 ${bodyLeft} 0
                Z
            `;
            break;

        case 'NOT':
            gatePath = `
                M ${bodyLeft} 0
                L 86 35
                L ${bodyLeft} 70
                Z
            `;
            break;
    }

    const bubbleCenterX = type === 'NOT' ? 92 : 105;

    return (
        <Svg
            width={112}
            height={70}
            style={{
                position: 'absolute',
                left: x - GATE_WIDTH / 2,
                top: y - GATE_HEIGHT / 2,
            }}
        >
            {/* XOR has an additional curved input line. */}
            {isXor && (
                <Path
                    d="M 0 0 Q 20 35 0 70"
                    stroke="#60A5FA"
                    strokeWidth={2.5}
                    fill="none"
                />
            )}

            {/* Main gate shape */}
            <Path
                d={gatePath}
                fill="#1E293B"
                stroke="#60A5FA"
                strokeWidth={2.5}
                strokeLinejoin="round"
            />

            {/* Inversion bubble */}
            {isInverting && (
                <Circle
                    cx={bubbleCenterX}
                    cy={middle}
                    r={6}
                    fill="#1E293B"
                    stroke="#60A5FA"
                    strokeWidth={2}
                />
            )}

            {/* Gate label */}
            <SvgText
                x={type === 'NOT' ? 43 : 51}
                y={39}
                fill="#FFFFFF"
                fontSize={type.length > 3 ? 10 : 11}
                fontWeight="bold"
                textAnchor="middle"
            >
                {type}
            </SvgText>
        </Svg>
    );
}