package br.com.techgol.app.services;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.email.EnviadorEmail;
import br.com.techgol.app.model.Funcionario;

@Service
public class TwoFactorAuthService {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private EnviadorEmail enviador;
    
    private static final SecureRandom random = new SecureRandom();

    public void gerarEEnviarCodigo(Funcionario funcionario) {
        // Gera um código aleatório de 6 dígitos
        String codigo = String.format("%06d", random.nextInt(1_000_000));
        
        funcionario.setCode(codigo);
        funcionarioService.atualizarCode(funcionario);

        // Envia o código por e-mail
        String assunto = "Seu código de verificação";
        String mensagem = "Olá " + funcionario.getNomeFuncionario() + ",\n\n"
                + "Seu código de verificação é: " + codigo + "\n\n"
                + "Este código é válido por 10 minutos.\n\n"
                + "Atenciosamente,\nEquipe de Segurança";

        enviador.enviar2fa(funcionario.getEmail(), assunto, mensagem);
    }
}
