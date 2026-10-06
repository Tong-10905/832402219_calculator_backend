package com.fzu.calculator.model;

/**
 * Data transfer object returned by GET /api/history.
 * Converts the database entity CalculationHistory into a JSON-friendly structure.
 */
public record HistoryEntry(Long id, String expression, String result, String time) {

    public static HistoryEntry from(CalculationHistory record) {
        return new HistoryEntry(record.getId(), record.getExpression(), record.getResult(), record.getCreatedAt());
    }
}
