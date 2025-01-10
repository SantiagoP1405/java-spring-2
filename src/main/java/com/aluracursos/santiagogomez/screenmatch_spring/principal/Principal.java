package com.aluracursos.santiagogomez.screenmatch_spring.principal;

import com.aluracursos.santiagogomez.screenmatch_spring.model.DatosEpisodio;
import com.aluracursos.santiagogomez.screenmatch_spring.model.DatosSerie;
import com.aluracursos.santiagogomez.screenmatch_spring.model.DatosTemporadas;
import com.aluracursos.santiagogomez.screenmatch_spring.model.Episodio;
import com.aluracursos.santiagogomez.screenmatch_spring.service.ConsumoAPI;
import com.aluracursos.santiagogomez.screenmatch_spring.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner input = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String url = "https://www.omdbapi.com/?t=";
    private final String apiKey = "&apikey=98314eb2";
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    public void muestraMenu(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = input.nextLine();
        //System.out.println(url + nombreSerie.replace(" ", "+") + apiKey);
        var json = consumoApi.obtenerDatos(url + nombreSerie.replace(" ", "+") + apiKey);
        var datos = convierteDatos.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalTemporadas() ; ++i) {
            //System.out.println(url + nombreSerie.replace(" ", "+") + "&Seasons=" + i + apiKey);
            json = consumoApi.obtenerDatos(url+nombreSerie.replace(" ", "+")+"&Season=" + i + apiKey);
            var datosTemporadas = convierteDatos.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        //temporadas.forEach(System.out::println);

       /* for (int i = 0; i < datos.totalTemporadas(); ++i) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); ++j) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo()))); /*Usa un lambda para iterar sobre cada elemento. Para cada temporada t, se ejecuta el método episodios, y por cada episodio de cada temporada se imprime su título*/

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()) //Se simplicifan los datos complejos de los episodios de una lista más manejable
                .collect(Collectors.toList()); //recopila los elementos de un flujo (stream) y los convierte en una lista.

        /*System.out.println("Top 5 mejores episodios");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primer filtro (N/A)" + e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("Segundo ordenación (M>m)" + e))
                .limit(5)
                .map(e->e.titulo().toUpperCase())
                .peek(e -> System.out.println("Tercer filtro Mayúscula(m>M)" + e))
                .forEach(System.out::println);
        */
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());
        //episodios.forEach(System.out::println);

        /*System.out.println("Indica el año a partir del cual deseas ver los episodios:");
        var fecha = input.nextInt();
        input.nextLine();*/

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        //LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
        /*episodios.stream()
                .filter(e -> e.getFechaLanzamiento() != null && e.getFechaLanzamiento().isAfter(fechaBusqueda))
                .forEach(e-> System.out.println(
                        "Temporada" + e.getTemporada() +
                                "Episodio " + e.getTitulo() +
                                " Fecha de Lanzammiento " + e.getFechaLanzamiento().format(dtf)
                ));*/
        /*System.out.println("Escribe el pedazo del titulo que deseas ver");
        var pedazoTitulo = input.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()){
            System.out.println("Encontrado");
            System.out.println(episodioBuscado.get());
        }
        else{
            System.out.println("No encontrado");
        }*/
        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media: " + est.getAverage());
        System.out.println("Episodio mejor evaluado: " + est.getMax());
        System.out.println("Episodio peor evaluado: " + est.getMin());
    }
}