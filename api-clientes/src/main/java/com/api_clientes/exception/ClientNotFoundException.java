package com.api_clientes.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String code){
        super(code);
    }

    }

