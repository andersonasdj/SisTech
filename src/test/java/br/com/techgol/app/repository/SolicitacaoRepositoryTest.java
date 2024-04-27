package br.com.techgol.app.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.techgol.app.model.enums.Status;
import br.com.techgol.app.orm.SolicitacaoProjecaoEntidadeComAtributos;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SolicitacaoRepositoryTest {
	
	@Autowired
	private SolicitacaoRepository repository;

	@Test
	@DisplayName("Lista as solicitacoes do dia atual")
	void testListarSolicitacoesAgendadasDoDia() {
		
		var data = LocalDate.now();
		LocalDateTime inicio = LocalDateTime.of(data.getYear(), data.getMonth(), data.getDayOfMonth(), 00, 00, 00);
		LocalDateTime fim = LocalDateTime.of(data.getYear(), data.getMonth(), data.getDayOfMonth(), 23, 59, 59);
		List<SolicitacaoProjecaoEntidadeComAtributos> solicitacoes = repository.listarSolicitacoesAgendadasDoDia(Status.FINALIZADO, false, inicio, fim);
		assertThat(solicitacoes.isEmpty());
	}
	

}
