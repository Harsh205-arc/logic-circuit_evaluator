# Logic Circuit Evaluator

This is a project intended to make understanding Boolean expressions easier with the help of truth tables and logic circuit diagrams. The website is intended to be used by engineering students to better understand Boolean logic and digital circuits.

## Overview

The project processes a Boolean expression through several stages:

1. The user inputs a Boolean expression.
2. The expression is tokenized into individual tokens.
3. The tokens are consumed by a recursive-descent parser.
4. The parser constructs an Abstract Syntax Tree (AST).
5. The evaluator evaluates the AST according to Boolean logic.
6. The variable collector identifies all variables present in the expression.
7. Variable values can then be provided for evaluation.
8. A truth table and logic circuit diagram will be generated from the expression.

### Processing Flow

Boolean Expression
        ↓
    Tokenizer
        ↓
      Tokens
        ↓
      Parser
        ↓
       AST
        ↓
Variable Collector
        ↓
Variable Assignments
        ↓
    Evaluator
        ↓
   Boolean Result
        ↓
 Truth Table / Circuit


## Core Features

* Recursive-descent parser
* Boolean operators:

  * AND
  * OR
  * NOT
  * XOR
  * NAND
  * XNOR

* Parentheses
* Operator precedence
* Abstract Syntax Tree (AST) generation
* Variable collection
* Boolean expression evaluation

## Operator Precedence

The parser currently uses the following operator precedence, from highest to lowest:

NOT
AND / NAND
XOR / XNOR
OR


## Project Structure


src/
└── main/
    └── java/
        └── io/github/harsh/
            ├── App.java
            ├── Tokenizer.java
            ├── Parser.java
            ├── Evaluator.java
            ├── VariableCollector.java
            └── ast/
                ├── Expression.java
                ├── BinaryExpression.java
                ├── UnaryExpression.java
                └── VariableExpression.java


## Roadmap

The following roadmap illustrates where the project is headed and what has been achieved so far.

* [x] Tokenizer
* [x] Recursive-descent parser
* [x] Abstract Syntax Tree
* [x] Evaluator
* [x] Variable collector
* [ ] Interactive variable input
* [ ] Truth-table generation
* [ ] Boolean expression simplification
* [ ] Circuit representation
* [ ] Circuit diagram generation
* [ ] Backend API
* [ ] Web frontend
