package com.aluracursos.screenmatch.principal;

import java.util.Arrays;

import java.util.List;

public class EjemploStreams {
    List<String> nombres = Arrays.asList("Ana", "Pedro", "Juan", "Maria", "Jose");

    public void muestraEjemplo(){

        nombres.stream()
                .sorted().limit(2)
                .filter(n -> n.startsWith("J"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
