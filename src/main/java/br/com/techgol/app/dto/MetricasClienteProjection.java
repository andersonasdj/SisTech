package br.com.techgol.app.dto;

import java.math.BigDecimal;

public interface MetricasClienteProjection {
	Long getClienteId();
    Long getQtdFechadas();
    Long getQtdAtualizados();
    Long getQtdAbertos();
    BigDecimal getTotalMinutos();

}
