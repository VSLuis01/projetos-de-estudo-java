package br.com.luisedu.libraryapi.exceptions;

public class OperacaoNaoPermitidaExpection extends RuntimeException {
    public OperacaoNaoPermitidaExpection(String message) {
        super(message);
    }
}
