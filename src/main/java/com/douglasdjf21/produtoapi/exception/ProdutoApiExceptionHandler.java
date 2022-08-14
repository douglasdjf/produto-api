package com.douglasdjf21.produtoapi.exception;

import com.douglasdjf21.produtoapi.exception.model.ErroException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class ProdutoApiExceptionHandler  {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroException> handleValidationException(MethodArgumentNotValidException ex,HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErroException.builder()
                                    .timestamp(LocalDateTime.now())
                                    .error("Erro de validação")
                                    .message(ex.getMessage())
                                    .status(HttpStatus.BAD_REQUEST.value())
                                    .path(request.getRequestURI())
                                    .build());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroException> handleFormatMessageException(HttpMessageNotReadableException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErroException.builder()
                .timestamp(LocalDateTime.now())
                .error("Formato de mensagem inválido")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroException> handleValidationDataException(DataIntegrityViolationException ex, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErroException.builder()
                .timestamp(LocalDateTime.now())
                .error("Erro persistência de dados")
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .build());
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroException> handleAuthenticationException(AuthenticationException authenticationException, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErroException.builder()
                .timestamp(LocalDateTime.now())
                .error("Erro autenticação")
                .message(authenticationException.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(BadRequestHeaderException.class)
    public ResponseEntity<ErroException> handleHeaderRequestException(BadRequestHeaderException badRequestHeaderException,  HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErroException.builder()
                .timestamp(LocalDateTime.now())
                .error("Error header requerido")
                .message(badRequestHeaderException.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build());

    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroException> handleValidacaoException(ValidacaoException ex,  HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErroException.builder()
                .timestamp(LocalDateTime.now())
                .error("Erro validação")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build());

    }
}
