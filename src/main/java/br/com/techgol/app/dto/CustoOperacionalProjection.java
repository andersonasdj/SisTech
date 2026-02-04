package br.com.techgol.app.dto;

import java.math.BigDecimal;

public interface CustoOperacionalProjection {
	Long getClienteId();
    Long getFuncionarioId();
    BigDecimal getTotalMinutos();
}
