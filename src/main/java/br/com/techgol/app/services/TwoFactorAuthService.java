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
        String codigo = String.format("%06d", random.nextInt(1_000_000));
        
        funcionario.setCode(codigo);
        funcionario.setTwoFactorVerified(false);
        funcionarioService.atualizarCode(funcionario);

        String assunto = "Seu código de verificação";
        String mensagem = "Olá, " + funcionario.getNomeFuncionario() + ".<br><br>"
                + "Seu código de verificação é: <b>" + codigo + "</b><br><br>"
                + "Este código é pessoal e intransferível. Não compartilhe com ninguém. <br><br>Atenciosamente, <br>Equipe de Segurança";

        enviador.enviar2fa(funcionario.getEmail(), assunto, mensagem);
    }
}
