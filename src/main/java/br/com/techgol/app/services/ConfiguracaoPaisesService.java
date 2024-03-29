package br.com.techgol.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoPaises;
import br.com.techgol.app.model.ConfiguracaoPaises;
import br.com.techgol.app.repository.ConfiguracaoPaisesRepository;
import jakarta.transaction.Transactional;

@Service
public class ConfiguracaoPaisesService {
	
	
	
	@Autowired
	private ConfiguracaoPaisesRepository paisesRepository;
	
	@Transactional
	public List<ConfiguracaoPaises> checarPaises(DtoPaises dados) {
		
		List<ConfiguracaoPaises> lista = paisesRepository.findAll();
		List<ConfiguracaoPaises> listaEnviada = new ArrayList<>();
		
		dados.paises().forEach(d -> {
			listaEnviada.add(new ConfiguracaoPaises(d, true));
		});
		
		lista.forEach(l -> {
			l.setStatus(false);
		});
		
		listaEnviada.forEach(l -> {
			ConfiguracaoPaises c = paisesRepository.findByPais(l.getPais());
			c.setStatus(true);
		});
		
		return lista;
	}

	public List<ConfiguracaoPaises> listarPaises() {
		return paisesRepository.listarPaises();
	}

	public void salvar(ConfiguracaoPaises configuracaoPaises) {

		paisesRepository.save(configuracaoPaises);
	}
	
	public int existeConfig() {
		return paisesRepository.existsConfigPaises();
	}
	
	
	public List<String> paisesAtivos(){
		return paisesRepository.listarPaisesString();
	}
	
	

}
