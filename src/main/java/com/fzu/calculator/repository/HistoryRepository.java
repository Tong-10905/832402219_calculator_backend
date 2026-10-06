package com.fzu.calculator.repository;

import com.fzu.calculator.model.CalculationHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * History data access interface.
 * By extending JpaRepository, Spring Data JPA automatically generates the
 * implementation of save / findById / deleteById etc. at run time, so no SQL needs
 * to be written by hand. Only the "query all records ordered by id descending"
 * method is declared here; Spring parses the method name to generate the query.
 */
public interface HistoryRepository extends JpaRepository<CalculationHistory, Long> {

    List<CalculationHistory> findAllByOrderByIdDesc();
}
