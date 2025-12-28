package com.matheuss.controle_estoque_api.exception;

import java.time.Instant;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class )
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Violação de integridade de dados. Um valor único (como CNPJ ou Etiqueta de Ativo) já existe.";
        
        // Tenta extrair uma mensagem mais específica do erro do banco de dados
        if (ex.getCause() != null && ex.getCause().getCause() != null) {
            String causeMessage = ex.getCause().getCause().getMessage();
            if (causeMessage.contains("Key (cnpj)")) {
                message = "O CNPJ informado já está cadastrado.";
            } else if (causeMessage.contains("Key (asset_tag)")) {
                message = "A Etiqueta de Ativo (Asset Tag) informada já está cadastrada.";
            } else if (causeMessage.contains("Key (serial_number)")) {
                message = "O Número de Série informado já está cadastrado.";
            }
        }

        Map<String, Object> body = Map.of(
            "timestamp", Instant.now(),
            "status", HttpStatus.CONFLICT.value(), // 409
            "error", "Conflict",
            "message", message
        );

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
