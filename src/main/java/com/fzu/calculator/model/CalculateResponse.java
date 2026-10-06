package com.fzu.calculator.model;

/**
 * Unified response body of POST /api/calculate.
 * On success: {"success": true,  "expression": "(1+2)*3", "result": 9}
 * On failure: {"success": false, "expression": "1++",     "message": "Invalid expression"}
 */
public record CalculateResponse(boolean success, String expression, Double result, String message) {

    public static CalculateResponse ok(String expression, double result) {
        return new CalculateResponse(true, expression, result, null);
    }

    public static CalculateResponse error(String expression, String message) {
        return new CalculateResponse(false, expression, null, message);
    }
}
