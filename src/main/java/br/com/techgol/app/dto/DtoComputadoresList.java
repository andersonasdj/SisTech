package br.com.techgol.app.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.techgol.app.model.Computador;

public record DtoComputadoresList(
		Long id,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime last_seen,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime agent_install_date,
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime last_boot_time,
		String type,
		String status,
		String name,
		String address,
		String os,
		String platform,
		String mac,
		String serial,
		String device_name,
		String cpu,
		String disk,
		String manufacturer,
		String ram,
		String cliente,
		String username
		
		) {
	
	public DtoComputadoresList(Computador c) {
		this(c.getId(), c.getLast_seen(), c.getAgent_install_date(), c.getLast_boot_time(), c.getType(), c.getStatus(),
				c.getName(), c.getAddress(), c.getOs(), c.getPlatform(), c.getMac(), c.getSerial(), c.getDevice_name(), c.getCpu(),
				c.getDisk(), c.getManufacturer(), c.getRam(), c.getCliente().getNomeCliente(), c.getUsername());
	}

}
