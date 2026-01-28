package com.matheuss.controle_estoque_api.domain.history;

public enum HistoryEventType {
    CRIACAO("Criação"),
    ALOCACAO("Alocação"),
    DEVOLUCAO("Devolução"),
    ATUALIZACAO("Atualização"), // ADICIONADO
    DESCARTE("Descarte");       // ADICIONADO

    private final String description;

    HistoryEventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
