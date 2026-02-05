package br.com.techgol.app.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

import br.com.techgol.app.dto.DtoRendimentosClientes;

public record DtoRelatorioRendimentoClientes(
	    List<DtoRendimentosClientes> clientes,
	    BigDecimal custoOperacionalTotal,
	    Long tempoTotalMinutos
	) {}

