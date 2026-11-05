import CircuitDiagram from '../components/circuitDiagram';

import type {
    CircuitDiagram as CircuitDiagramData,
} from '../../services/circuitService';
import { generateCircuit } from '../../services/circuitService';
import { useState } from 'react';


import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  ActivityIndicator,
} from 'react-native';

import {
  evaluateExpression,
  TruthTableRow,
} from '../../services/evaluationService';

export default function HomeScreen() {

  const [circuit, setCircuit] = useState<CircuitDiagramData | null>(null);
  const [circuitLoading, setCircuitLoading] = useState(false);

  // Stores the expression entered by the user
  const [expression, setExpression] = useState('');

  // Stores the truth table returned by Spring Boot
  const [results, setResults] = useState<TruthTableRow[]>([]);

  // Tracks whether the API request is running
  const [loading, setLoading] = useState(false);

  // Stores any error returned by the backend
  const [error, setError] = useState('');

  // Adds an operator to the expression
  const addOperator = (operator: string) => {
    setExpression((previous) => previous + operator);
  };

  
  // Sends the expression to our backend
  const handleEvaluate = async () => {

    if (!expression.trim()) {
      setError('Please enter a Boolean expression.');
      setResults([]);
      return;
    }

    try {
      setLoading(true);
      setError('');
      setResults([]);

      const data = await evaluateExpression(expression);

      // Store the returned truth table
      setResults(data);

    } catch (err) {

      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError('Something went wrong.');
      }

    } finally {
      setLoading(false);
    }
  };
  
  const handleGenerateCircuit = async () => {
    if (!expression.trim()) {
        setError('Please enter a Boolean expression.');
        return;
    }

    try {
        setCircuitLoading(true);
        setError('');
        setCircuit(null);

        const data = await generateCircuit(expression);

        setCircuit(data);
    } catch (err) {
        if (err instanceof Error) {
            setError(err.message);
        } else {
            setError('Circuit generation failed.');
        }
    } finally {
        setCircuitLoading(false);
    }
  };

  // Extract variable names from the first truth table row
  const variables = results.length > 0
    ? Object.keys(results[0].variables)
    : [];

  return (
    <ScrollView
      contentContainerStyle={styles.container}
      keyboardShouldPersistTaps="handled"
    >

      {/* Application heading */}
      <View style={styles.header}>
        <Text style={styles.appName}>LOGIC LAB</Text>

        <Text style={styles.heading}>
          Boolean Expression Evaluator
        </Text>

        <Text style={styles.subtitle}>
          Build and evaluate logical expressions
        </Text>
      </View>

      {/* Expression input section */}
      <View style={styles.inputSection}>

        <Text style={styles.label}>
          ENTER EXPRESSION
        </Text>

        <TextInput
          style={styles.input}
          placeholder="Example: A AND B"
          placeholderTextColor="#888"
          value={expression}
          onChangeText={(value) => {
            setExpression(value);
            setError('');
            setResults([]);
          }}
          autoCapitalize="characters"
        />

        {/* Operator buttons */}
        <View style={styles.operatorContainer}>
          {['AND', 'OR', 'NOT', 'XOR', 'NAND', 'XNOR'].map(
            (operator) => (
              <TouchableOpacity
                key={operator}
                style={styles.operatorButton}
                onPress={() => addOperator(` ${operator} `)}
              >
                <Text style={styles.operatorText}>
                  {operator}
                </Text>
              </TouchableOpacity>
            )
          )}
        </View>

        {/* Parentheses and clear */}
        <View style={styles.operatorContainer}>

          {['(', ')'].map((symbol) => (
            <TouchableOpacity
              key={symbol}
              style={styles.operatorButton}
              onPress={() => addOperator(symbol)}
            >
              <Text style={styles.operatorText}>
                {symbol}
              </Text>
            </TouchableOpacity>
          ))}

          <TouchableOpacity
            style={styles.clearButton}
            onPress={() => {
              setExpression('');
              setResults([]);
              setError('');
            }}
          >
            <Text style={styles.operatorText}>
              CLEAR
            </Text>
          </TouchableOpacity>

        </View>

        {/* Evaluate button */}
        <TouchableOpacity
          style={[
            styles.evaluateButton,
            loading && styles.disabledButton,
          ]}
          onPress={handleEvaluate}
          disabled={loading}
        >
          {loading ? (
            <ActivityIndicator color="#FFFFFF" />
          ) : (
            <Text style={styles.evaluateText}>
              Evaluate Expression
            </Text>
          )}
        </TouchableOpacity>

        <TouchableOpacity
            style={styles.evaluateButton}
            onPress={handleGenerateCircuit}
            disabled={circuitLoading}
        >
            {circuitLoading ? (
                <ActivityIndicator color="#FFFFFF" />
            ) : (
                <Text style={styles.evaluateText}>
                    Generate Circuit
                </Text>
            )}
      </TouchableOpacity>
      </View>
      {/* Results section */}
      <View style={styles.resultsSection}>

        <Text style={styles.resultsHeading}>
          Evaluation Results
        </Text>

        {/* Error message */}
        {error !== '' && (
          <Text style={styles.errorText}>
            {error}
          </Text>
        )}

        {/* Truth table */}
        {results.length > 0 && (
          <ScrollView horizontal showsHorizontalScrollIndicator>

            <View>

              {/* Table headers */}
              <View style={styles.tableRow}>
                {variables.map((variable) => (
                  <View
                    key={variable}
                    style={styles.tableCell}
                  >
                    <Text style={styles.tableHeaderText}>
                      {variable}
                    </Text>
                  </View>
                ))}

                <View style={styles.tableCell}>
                  <Text style={styles.tableHeaderText}>
                    Result
                  </Text>
                </View>
              </View>

              {/* Table data */}
              {results.map((row, index) => (
                <View
                  key={index}
                  style={styles.tableRow}
                >

                  {variables.map((variable) => (
                    <View
                      key={variable}
                      style={styles.tableCell}
                    >
                      <Text style={styles.tableText}>
                        {row.variables[variable] ? '1' : '0'}
                      </Text>
                    </View>
                  ))}

                  <View style={styles.tableCell}>
                    <Text
                      style={[
                        styles.tableText,
                        styles.resultText,
                      ]}
                    >
                      {row.result ? '1' : '0'}
                    </Text>
                  </View>

                </View>
              ))}

            </View>

          </ScrollView>
        )}

        {/* Initial state */}
        {results.length === 0 && error === '' && !loading && (
          <Text style={styles.subtitle}>
            Your truth table will appear here.
          </Text>
        )}

      </View>
      {circuit && (
        <CircuitDiagram diagram={circuit} />
      )}
    </ScrollView>
  );

}

const styles = StyleSheet.create({

  container: {
    flexGrow: 1,
    backgroundColor: '#101827',
    padding: 24,
    paddingTop: 55,
  },

  header: {
    marginBottom: 35,
  },

  appName: {
    color: '#60A5FA',
    fontSize: 15,
    fontWeight: 'bold',
    letterSpacing: 3,
    marginBottom: 15,
  },

  heading: {
    color: '#FFFFFF',
    fontSize: 26,
    fontWeight: 'bold',
    marginBottom: 10,
  },

  subtitle: {
    color: '#94A3B8',
    fontSize: 14,
  },

  inputSection: {
    backgroundColor: '#1E293B',
    padding: 18,
    borderRadius: 15,
    marginBottom: 25,
  },

  label: {
    color: '#CBD5E1',
    fontSize: 12,
    fontWeight: 'bold',
    marginBottom: 12,
    letterSpacing: 1,
  },

  input: {
    backgroundColor: '#334155',
    color: '#FFFFFF',
    padding: 15,
    borderRadius: 10,
    fontSize: 17,
    marginBottom: 18,
  },

  operatorContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginBottom: 15,
  },

  operatorButton: {
    backgroundColor: '#334155',
    paddingVertical: 11,
    paddingHorizontal: 13,
    borderRadius: 8,
  },

  operatorText: {
    color: '#FFFFFF',
    fontWeight: 'bold',
    fontSize: 12,
  },

  clearButton: {
    backgroundColor: '#991B1B',
    paddingVertical: 11,
    paddingHorizontal: 13,
    borderRadius: 8,
  },

  evaluateButton: {
    backgroundColor: '#2563EB',
    padding: 16,
    borderRadius: 10,
    alignItems: 'center',
    marginTop: 5,
    minHeight: 52,
    justifyContent: 'center',
  },

  evaluateText: {
    color: '#FFFFFF',
    fontSize: 15,
    fontWeight: 'bold',
  },

  disabledButton: {
    opacity: 0.6,
  },

  resultsSection: {
    backgroundColor: '#1E293B',
    padding: 18,
    borderRadius: 15,
    marginBottom: 25,
  },

  resultsHeading: {
    color: '#FFFFFF',
    fontSize: 19,
    fontWeight: 'bold',
    marginBottom: 15,
  },

  errorText: {
    color: '#F87171',
    fontSize: 14,
    marginBottom: 10,
  },

  tableRow: {
    flexDirection: 'row',
  },

  tableCell: {
    width: 75,
    padding: 12,
    borderWidth: 1,
    borderColor: '#475569',
    alignItems: 'center',
    justifyContent: 'center',
  },

  tableHeaderText: {
    color: '#60A5FA',
    fontWeight: 'bold',
    fontSize: 14,
  },

  tableText: {
    color: '#FFFFFF',
    fontSize: 15,
  },

  resultText: {
    fontWeight: 'bold',
    color: '#4ADE80',
  },

});
