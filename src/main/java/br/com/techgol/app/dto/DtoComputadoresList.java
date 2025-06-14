package br.com.techgol.app.dto;

import java.time.LocalDateTime;
import java.util.List;

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
		List<String> disk,
	    String manufacturer,
		String ram,
		String cliente,
		String username,
		String ramAvailable,
		List<String> diskAvailable,
		List<String> ipAddresses,
		long networkSpeedMbps,
		String comment,
		long systemUptimeSeconds,
		List<String> memorySlots,
		String gateway,
		List<String> dnsServers,
		List<String> gpus,
		String biosVersion,
		String biosVendor,
		String biosReleaseDate,
		int monitores,
		String uuid,
		boolean isVirtualMachine,
		String domain,
		String deviceType
		) {
	
	public DtoComputadoresList(Computador c) {
		this(c.getId(), c.getLast_seen(), c.getAgent_install_date(), c.getLast_boot_time(), c.getType(), c.getStatus(),
				c.getName(), c.getAddress(), c.getOs(), c.getPlatform(), c.getMac(), c.getSerial(), c.getDevice_name(), c.getCpu(),
				c.getDisk(), c.getManufacturer(), c.getRam(), c.getCliente().getNomeCliente(), c.getUsername(),
				c.getRamAvailable(), c.getDiskAvailable(), c.getIpAddresses(), c.getNetworkSpeedMbps(), c.getComment(),
				c.getSystemUptimeSeconds(), c.getMemorySlots(), c.getGateway(), c.getDnsServers(), c.getGpus(),
				c.getBiosVersion(), c.getBiosVendor(), c.getBiosReleaseDate(), c.getMonitores(), c.getUuid(),
				c.isVirtualMachine(), c.getDomain(), c.getDeviceType()
				);
	}

}
