package com.douglasdjf21.produtoapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestHeaderException extends RuntimeException {

    public BadRequestHeaderException(String mensagem){
        super(mensagem);
    }
}
