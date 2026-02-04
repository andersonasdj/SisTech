package br.com.techgol.app.dto;

import java.math.BigDecimal;

public record DtoMetricasClienteRaw(
	    Long clienteId,
	    Long qtdFechadas,
	    Long qtdAtualizados,
	    Long qtdAbertos,
	    BigDecimal totalMinutos
	) {}

