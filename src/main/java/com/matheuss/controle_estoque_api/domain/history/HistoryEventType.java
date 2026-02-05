package com.matheuss.controle_estoque_api.domain.history;

public enum HistoryEventType {
    CRIACAO("Criação"),
    ALOCACAO("Alocação"),
    DEVOLUCAO("Devolução"),
    ATUALIZACAO("Atualização"),
    DESCARTE("Descarte"),
    INSTALACAO("Instalação"); 

    private final String description;

    HistoryEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
