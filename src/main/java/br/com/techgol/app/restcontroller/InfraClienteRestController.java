package br.com.techgol.app.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoInfraClienteRequest;
import br.com.techgol.app.dto.DtoInfraClienteResponse;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.model.ClienteInfraestrutura;
import br.com.techgol.app.repository.ClienteRepository;
import br.com.techgol.app.repository.InfraClienteRepository;

@RestController
@RequestMapping("api/v1/cliente/infra")
public class InfraClienteRestController {
	
	@Autowired
	InfraClienteRepository repository;
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<?> buscarPorCliente(@PathVariable Long clienteId) {

		 ClienteInfraestrutura infra = repository.findByClienteId(clienteId)
	        .orElseGet(() -> {

	            Cliente cliente = clienteRepository.findById(clienteId)
	                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

	            ClienteInfraestrutura novaInfra = new ClienteInfraestrutura();
	            novaInfra.setCliente(cliente);

	            return repository.save(novaInfra);
	        });

	    return ResponseEntity.ok(toResponse(infra));
	}

	
	@PostMapping("/{clienteId}")
    public ResponseEntity<?> criar(
            @PathVariable Long clienteId,
            @RequestBody DtoInfraClienteRequest dto) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        ClienteInfraestrutura infra = new ClienteInfraestrutura();
        infra.setCliente(cliente);
        aplicarDto(infra, dto);

        repository.save(infra);

        return ResponseEntity.ok(toResponse(infra));
    }
	
	 @PutMapping("/{clienteId}")
	    public ResponseEntity<?> atualizar(
	            @PathVariable Long clienteId,
	            @RequestBody DtoInfraClienteRequest dto) {

		 ClienteInfraestrutura infra = repository.findByClienteId(clienteId)
	                .orElseThrow(() -> new RuntimeException("Infraestrutura não encontrada"));

	        aplicarDto(infra, dto);
	        repository.save(infra);

	        return ResponseEntity.ok(toResponse(infra));
	    }
	
	
	private void aplicarDto(ClienteInfraestrutura infra, DtoInfraClienteRequest dto) {

        infra.setFirewallFabricante(dto.firewallFabricante());
        infra.setFirewallModelo(dto.firewallModelo());

        infra.setLinkPrincipal(dto.linkPrincipal());
        infra.setVelocidadePrincipal(dto.velocidadePrincipal());
        infra.setLinkBackup(dto.linkBackup());
        infra.setVelocidadeBackup(dto.velocidadeBackup());
        infra.setLinkTerciario(dto.linkTerciario());
        infra.setVelocidadeTerciario(dto.velocidadeTerciario());

        infra.setLan(dto.lan());
        infra.setVlan(dto.vlan());
        infra.setWifi(dto.wifi());
        infra.setVpn(dto.vpn());
        infra.setDominio(dto.dominio());

        infra.setAntivirus(dto.antivirus());
        infra.setBackup(dto.backup());

        infra.setObservacoes(dto.observacoes());
        
        infra.setPlataformaEmail(dto.plataformaEmail());
        infra.setTecnologiaEmail(dto.tecnologiaEmail());
    }
	
	private DtoInfraClienteResponse toResponse(ClienteInfraestrutura infra) {

        return new DtoInfraClienteResponse(
                infra.getCliente().getId(),

                infra.getFirewallFabricante(),
                infra.getFirewallModelo(),

                infra.getLinkPrincipal(),
                infra.getVelocidadePrincipal(),
                infra.getLinkBackup(),
                infra.getVelocidadeBackup(),
                infra.getLinkTerciario(),
                infra.getVelocidadeTerciario(),

                infra.getLan(),
                infra.getVlan(),
                infra.getWifi(),
                infra.getVpn(),
                infra.getDominio(),

                infra.getAntivirus(),
                infra.getBackup(),

                infra.getObservacoes(),
                infra.getPlataformaEmail(),
                infra.getTecnologiaEmail()
          
        );
    }

}
