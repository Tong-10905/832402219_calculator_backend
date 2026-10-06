package com.fzu.calculator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Calculation history entity, mapped to the calculation_history table in the SQLite database.
 * Each successful calculation inserts one record here:
 * id is auto-incremented by the database, expression stores the original expression,
 * result stores the result returned by the back end, and createdAt stores the calculation time.
 */
@Entity
@Table(name = "calculation_history")
public class CalculationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String expression;

    @Column(nullable = false)
    private String result;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    public CalculationHistory() {
    }

    public CalculationHistory(String expression, String result, String createdAt) {
        this.expression = expression;
        this.result = result;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
