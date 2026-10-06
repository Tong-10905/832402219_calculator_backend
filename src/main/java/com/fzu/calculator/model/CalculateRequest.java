package com.fzu.calculator.model;

/**
 * Request body of POST /api/calculate.
 * The front end only sends the expression text; all calculation is done by the back end.
 * Example: {"expression": "(1+2)*3"}
 */
public record CalculateRequest(String expression) {
}
