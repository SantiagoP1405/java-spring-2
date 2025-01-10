package com.aluracursos.santiagogomez.screenmatch_spring.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Santiago", "Sebastián", "Claudia", "Ohmar", "Paulina");
        nombres.stream() //Sirve para realizar operaciones de forma encadenada
                .sorted()
                //.limit(4)
                .filter(n -> n.startsWith("S"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }
}
