package br.com.techgol.app.ia;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AIEmailAnaliseSolicitacao {

    private String descricao;
    private String solicitante;
    private String usuarioAfetado;
    private String local;
    private String categoria;
    private String classificacao;
    private String criticidade;

}
