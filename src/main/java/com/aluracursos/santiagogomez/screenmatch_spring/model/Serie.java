package com.aluracursos.santiagogomez.screenmatch_spring.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

@Entity //Esto va a ser una entidad de la base de datos
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Para que se genere automáticamente y se genere un número autoiincremental
    private Long id;
    @Column(unique = true)
    private String titulo; //json alias asís e llama en el json de la api. Al mapearlo en la aplicaicón se convierte en título
    private Integer totalTemporadas;
    private Double evaluacion;
    @Enumerated(EnumType.STRING) //Se indica el tipo de enum
    private Categoria genero; //permite definir un conjunto de constantes relacionadas. ayuda a evitar errores
    private String sinopsis;
    private String poster;
    private String casting;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL) //Relación de 1 a muchos
    private List<Episodio> episodios;

    public Serie(){

    }

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim()); //Cada que se encuentra una coma, se crea una lista y se toma la primera posición
        this.poster = datosSerie.poster();
        this.casting = datosSerie.cast();
        this.sinopsis = datosSerie.sinopsis();
        //this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSerie.sinopsis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCasting() {
        return casting;
    }

    public void setCasting(String casting) {
        this.casting = casting;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
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
                ", cast='" + casting;
    }
}
