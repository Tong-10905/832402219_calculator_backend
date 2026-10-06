package com.fzu.calculator.controller;

import com.fzu.calculator.model.CalculationHistory;
import com.fzu.calculator.model.CalculateRequest;
import com.fzu.calculator.model.CalculateResponse;
import com.fzu.calculator.model.HistoryEntry;
import com.fzu.calculator.repository.HistoryRepository;
import com.fzu.calculator.service.CalculatorService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API controller. All front-end requests enter here:
 * POST   /api/calculate    -> calculate and save a history record
 * GET    /api/history      -> query the calculation history
 * DELETE /api/history/{id} -> delete the history record with the specified id
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CalculatorController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CalculatorService calculatorService;
    private final HistoryRepository historyRepository;

    public CalculatorController(CalculatorService calculatorService, HistoryRepository historyRepository) {
        this.calculatorService = calculatorService;
        this.historyRepository = historyRepository;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateResponse> calculate(@RequestBody CalculateRequest request) {
        String expression = request == null || request.expression() == null ? "" : request.expression().trim();
        if (expression.isEmpty()) {
            return ResponseEntity.badRequest().body(CalculateResponse.error(expression, "Expression is empty"));
        }
        if (expression.length() > 200) {
            return ResponseEntity.badRequest().body(CalculateResponse.error(expression, "Expression is too long"));
        }
        try {
            double result = calculatorService.evaluate(expression);
            String time = LocalDateTime.now().format(TIME_FORMAT);
            historyRepository.save(new CalculationHistory(expression, formatResult(result), time));
            return ResponseEntity.ok(CalculateResponse.ok(expression, result));
        } catch (ArithmeticException e) {
            return ResponseEntity.badRequest().body(CalculateResponse.error(expression, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(CalculateResponse.error(expression, "Invalid expression"));
        }
    }

    @GetMapping("/history")
    public List<HistoryEntry> history() {
        return historyRepository.findAllByOrderByIdDesc().stream().map(HistoryEntry::from).toList();
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteHistory(@PathVariable Long id) {
        if (!historyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        historyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convert the result to a string stored in the database:
     * integers drop the trailing ".0", and decimals keep the full precision.
     */
    private String formatResult(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
