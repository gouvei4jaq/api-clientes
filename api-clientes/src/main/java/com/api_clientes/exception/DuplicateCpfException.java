package com.api_clientes.exception;

public class DuplicateCpfException extends RuntimeException {

    public DuplicateCpfException(String code){
        super(code);
    }

    }

