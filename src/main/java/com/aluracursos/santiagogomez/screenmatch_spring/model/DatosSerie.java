package com.aluracursos.santiagogomez.screenmatch_spring.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //Pasa por alto las propiedasdes que no están especificadas más abajo para evitar errores causados por los elementos que no se reconozcan en la comparación con la aplicación
public record DatosSerie(
        @JsonAlias("Title") String titulo, //json alias asís e llama en el json de la api. Al mapearlo en la aplicaicón se convierte en título
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String evaluacion,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Plot") String sinopsis,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Actors") String cast
) {
}
