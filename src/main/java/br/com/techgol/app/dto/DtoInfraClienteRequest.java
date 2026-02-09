package br.com.techgol.app.dto;

public record DtoInfraClienteRequest(

        String firewallFabricante,
        String firewallModelo,

        String linkPrincipal,
        String velocidadePrincipal,
        String linkBackup,
        String velocidadeBackup,
        String linkTerciario,
        String velocidadeTerciario,

        String lan,
        String vlan,
        String wifi,
        String vpn,
        String dominio,

        String antivirus,
        String backup,

        String observacoes,
        String plataformaEmail,
        String tecnologiaEmail
) {}
