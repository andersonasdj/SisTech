package br.com.techgol.app.orm;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ProjecaoComputadorCompleto {
	
	String getId();
	String getName();
	String getMac();
	String getOs();
	String getOsVersion();
	String getOsBuildNumber();
	String getOsArchitecture();
	String getTimeZone();
	String getCpu();
	String getCpuFrequencyGHz();
	String getRam();
	String getRamAvailable();
	List<String> getDisk();
    List<String> getDiskAvailable();
	String getPlatform();
	String getStatus();
	String getAddress();
	List<String> getIpAddresses();
	String getNetworkSpeedMbps();
	String getType();
	String getComment();
	String getSerial();
	String getManufacturer();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getLast_seen();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getAgent_install_date();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getLast_boot_time();
	String getUsername();
	String getSystemUptimeSeconds();
	List<String> getMemorySlots();
    String getGateway();
    List<String> getDnsServers();
    List<String> getGpus();
    String getBiosVersion();
    String getBiosVendor();
    String getBiosReleaseDate();
    String getUuid();
    String getDomain();
    String getDeviceType();
    String getAntivirus();
    List<String> getBitlockerRecoveryKeys();

}
