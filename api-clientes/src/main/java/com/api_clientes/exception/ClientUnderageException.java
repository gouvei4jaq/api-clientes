package com.api_clientes.exception;

public class ClientUnderageException extends RuntimeException {

    public ClientUnderageException(String code){
        super(code);
    }

    }

