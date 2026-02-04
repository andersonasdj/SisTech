package br.com.techgol.app.dto;

import java.math.BigDecimal;

public record DtoCustoOperacionalRaw(
	    Long clienteId,
	    Long funcionarioId,
	    BigDecimal totalMinutos
	) {}

