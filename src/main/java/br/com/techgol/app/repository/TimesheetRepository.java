package br.com.techgol.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.techgol.app.model.TimeSheet;

public interface TimesheetRepository extends JpaRepository<TimeSheet, Long> {

}
