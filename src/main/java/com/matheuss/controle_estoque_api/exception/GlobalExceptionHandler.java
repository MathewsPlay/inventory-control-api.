
package com.matheuss.controle_estoque_api.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class );

    /**
     * Handler para violação de integridade de dados (409 Conflict).
     * Mantém a lógica inteligente para identificar qual campo único foi violado.
     * Agora usa o ApiErrorResponse para padronizar a saída.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        String message = "Violação de integridade de dados. Um valor único já existe.";
        
        // Sua lógica original para extrair a causa específica do erro
        if (ex.getCause() != null && ex.getCause().getCause() != null) {
            String causeMessage = ex.getCause().getCause().getMessage().toLowerCase();
            if (causeMessage.contains("cnpj")) {
                message = "O CNPJ informado já está cadastrado no sistema.";
            } else if (causeMessage.contains("assettag") || causeMessage.contains("asset_tag")) {
                message = "A Etiqueta de Ativo (Asset Tag) informada já está cadastrada.";
            } else if (causeMessage.contains("serialnumber") || causeMessage.contains("serial_number")) {
                message = "O Número de Série informado já está cadastrado.";
            }
        }

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handler para entidade não encontrada (404 Not Found).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex, HttpServletRequest request) {
        
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler para violações de regras de negócio (400 Bad Request).
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(
            IllegalStateException ex, HttpServletRequest request) {
        
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler para erros de validação de DTOs via @Valid (400 Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        });

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação. Verifique os campos informados.",
                request.getRequestURI()
        );
        errorResponse.setValidationErrors(validationErrors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler "pega-tudo" para qualquer outra exceção não esperada (500 Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        log.error("Ocorreu um erro inesperado na requisição para '{}': ", request.getRequestURI(), ex);

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado no servidor. Entre em contato com o suporte.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
