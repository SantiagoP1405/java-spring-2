package com.aluracursos.santiagogomez.screenmatch_spring.model;

import com.aluracursos.santiagogomez.screenmatch_spring.service.ConsultaChatGPT;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.OptionalDouble;


public class Serie {
    private String titulo; //json alias asís e llama en el json de la api. Al mapearlo en la aplicaicón se convierte en título
    private Integer totalTemporadas;
    private Double evaluacion;
    private Categoria genero; //permite definir un conjunto de constantes relacionadas. ayuda a evitar errores
    private String sinopsis;
    private String poster;
    private String cast;

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim()); //Cada que se encuentra una coma, se crea una lista y se toma la primera posición
        this.poster = datosSerie.poster();
        this.cast = datosSerie.cast();
        this.sinopsis = datosSerie.sinopsis();
        //this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis());
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    @Override
    public String toString() {
        return
                "titulo='" + titulo +
                ", totalTemporadas=" + totalTemporadas +
                ", evaluacion=" + evaluacion +
                ", genero=" + genero +
                ", sinopsis='" + sinopsis +
                ", poster='" + poster +
                ", cast='" + cast;
    }
}
