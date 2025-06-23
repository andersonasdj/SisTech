package br.com.techgol.app.dto;

import br.com.techgol.app.model.Computador;

public record DtoComputadorResumido(
		Long id,
		String comment,
		boolean storageMonitor,
		boolean statusMonitor) {

	public DtoComputadorResumido(Computador c) {
		this(c.getId(), c.getComment(), c.isStorageMonitor(), c.isStatusMonitor());
	}

}
