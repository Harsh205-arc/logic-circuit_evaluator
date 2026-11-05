import React from 'react';
import Svg, { Path } from 'react-native-svg';

interface CircuitWireProps {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    color?: string;
}

export default function CircuitWire({
    x1,
    y1,
    x2,
    y2,
    color = '#94A3B8',
}: CircuitWireProps) {

    // Route horizontally first, then vertically, then horizontally.
    const middleX = (x1 + x2) / 2;

    const path = `
        M ${x1} ${y1}
        H ${middleX}
        V ${y2}
        H ${x2}
    `;

    return (
        <Path
            d={path}
            stroke={color}
            strokeWidth={2}
            fill="none"
            strokeLinecap="round"
            strokeLinejoin="round"
        />
    );
}