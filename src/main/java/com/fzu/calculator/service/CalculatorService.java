package com.fzu.calculator.service;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;

/**
 * Calculation service: the place where the core calculation logic is concentrated.
 *
 * Security design:
 * 1. First validate the input against a character whitelist; only digits, four
 *    arithmetic operators, parentheses, decimal points and spaces are allowed,
 *    and letters or other symbols are rejected directly;
 * 2. Then hand the expression to exp4j for parsing. exp4j builds its own abstract
 *    syntax tree (AST) for evaluation, and never executes the user input as
 *    program code, so there is no security hole like eval/exec.
 *
 * exp4j natively supports operator precedence, parentheses, unary plus/minus
 * (e.g. -5, 3*-2) and decimal numbers.
 */
@Service
public class CalculatorService {

    private static final double ROUND_FACTOR = 1e10;

    public double evaluate(String expression) {
        if (!expression.matches("[0-9+\\-*/().\\s]+")) {
            throw new IllegalArgumentException("Expression contains unsupported characters");
        }
        try {
            Expression parsed = new ExpressionBuilder(expression).build();
            double value = parsed.evaluate();
            if (Double.isInfinite(value) || Double.isNaN(value)) {
                throw new ArithmeticException("Division by zero");
            }
            // Round to 10 decimal places to avoid floating-point noise such as 0.1+0.2 = 0.30000000000000004
            return Math.round(value * ROUND_FACTOR) / ROUND_FACTOR;
        } catch (IllegalArgumentException | ArithmeticException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid expression");
        }
    }
}
