package br.com.techgol.app.orm;

import java.time.LocalDateTime;
import java.util.List;

import br.com.techgol.app.model.Cliente;

public interface ComputadoresProjecao {
	Long getId();
	String getName();
	String getMac();
	String getOs();
	String getOsVersion();
	String getOsBuildNumber();
	String getOsArchitecture();
	String getTimeZone();
	String getCpu();
	String getRam();
	String getRamAvailable();
	List<String> getDisk();
    List<String> getDiskAvailable();
	String getPlatform();
	String getStatus();
	String getAddress();
	List<String> getIpAddresses();
	String getType();
	String getComment();
	String getSerial();
	String getDevice_name();
	String getManufacturer();
	LocalDateTime getLast_seen();
	LocalDateTime getLast_boot_time();
	Cliente getCliente();
	String getUsername();
	long getSystemUptimeSeconds();
	List<String> getMemorySlots();
    String getDomain();
    String getDeviceType();
    String getAntivirus();
}
