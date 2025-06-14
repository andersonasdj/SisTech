package br.com.techgol.app.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "computador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Computador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", length = 300)
	private String name;
	
	@Column(name="mac", length = 60)
	private String mac;
	
	@Column(name="os", length = 300)
	private String os;
	
	private String osVersion;
	
	private String osBuildNumber;
	
	private String osArchitecture;
	
	private String timeZone;
	
	@Column(name="cpu_name", length = 200)
	private String cpu;
	
	private double cpuFrequencyGHz;
	
	private double cpuTemperatureCelsius;
	
	@Column(name="ram", length = 200)
	private String ram;
	
	private String ramAvailable;
	
	public List<String> disk;
	
    public List<String> diskAvailable;
	
	@Column(name="platform", length = 60)
	private String platform;
	
	@Column(name="status", length = 60)
	private String status;
	
	@Column(name="address", length = 300)
	private String address;
	
	private List<String> ipAddresses;
	
	private long networkSpeedMbps;
	
	@Column(name = "type", length = 60)
	private String type;
	
	@Column(name="comment", length = 60)
	private String comment;
	
	@Column(name="serial", length = 60)
	private String serial;
	
	@Column(name="device_name", length = 60)
	private String device_name;
	
	@Column(name="manufacturer", length = 200)
	private String manufacturer;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime last_seen;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime agent_install_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime last_boot_time;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Funcionario funcionario;
	
	@Column(name="username", length = 200)
	private String username;
	
	private long systemUptimeSeconds;
	private List<String> memorySlots;
    private String gateway;
    private List<String> dnsServers;
    private List<String> gpus;
    private String biosVersion;
    private String biosVendor;
    private String biosReleaseDate;
    private int monitores;
    private String uuid;
    private boolean isVirtualMachine;
    private String domain;
    public String deviceType;

}
