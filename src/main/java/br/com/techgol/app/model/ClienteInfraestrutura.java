package br.com.techgol.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "cliente_infraestrutura")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteInfraestrutura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* RELACIONAMENTO */
    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    /* FIREWALL */
    @Column(length = 100)
    private String firewallModelo;

    @Column(length = 100)
    private String firewallFabricante;

    /* INTERNET */
    @Column(length = 100)
    private String linkPrincipal;

    @Column(length = 50)
    private String velocidadePrincipal;

    @Column(length = 100)
    private String linkBackup;

    @Column(length = 50)
    private String velocidadeBackup;
    
    @Column(length = 100)
    private String linkTerciario;

    @Column(length = 50)
    private String velocidadeTerciario;

    /* REDE */
    @Column(length = 100)
    private String lan;
    
    @Column(length = 300)
    private String vlan;
    
    @Column(length = 100)
    private String wifi;
    
    @Column(length = 100)
    private String vpn;    
    
    @Column(length = 300)
    private String dominio;

    /* SEGURANÇA */
    @Column(length = 100)
    private String antivirus;

    @Column(length = 100)
    private String backup;

    /* OUTROS */
    @Column(length = 300)
    private String outrasFerramentas;

    @Column(length = 500)
    private String observacoes;
    
    @Column(length = 200)
    private String plataformaEmail;
    
    @Column(length = 200)
    private String tecnologiaEmail;
}
