package com.aluracursos.santiagogomez.screenmatch_spring.principal;

import com.aluracursos.santiagogomez.screenmatch_spring.model.*;
import com.aluracursos.santiagogomez.screenmatch_spring.respository.SerieRepository;
import com.aluracursos.santiagogomez.screenmatch_spring.service.ConsumoAPI;
import com.aluracursos.santiagogomez.screenmatch_spring.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner input = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String url = "https://www.omdbapi.com/?t=";
    private String apiKey = System.getenv("API_KEY_OMDB");
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series;
    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void muestraMenu(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar Series por Título
                    5 - Buscar el Top 5
                    6 - Buscar series por género
                    7 - Filtrar series por temporada y evaluación
                    8 - Buscar episodios por título
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = input.nextInt();
            input.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5();
                    break;
                case 6:
                    bucarPorGenero();
                    break;
                case 7:
                    buscarPorTemporadayEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosTitulo();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = input.nextLine();
        var json = consumoApi.obtenerDatos(url + nombreSerie.replace(" ", "+") + apiKey);
        System.out.println(json);
        DatosSerie datos = convierteDatos.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombe de la serie de la cual quieres ver los episodios: ");
        var nombresSerie = input.nextLine();
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombresSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(url + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + apiKey);
                DatosTemporadas datosTemporada = convierteDatos.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        }

    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repository.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }
    private void mostrarSeriesBuscadas() {
        series = repository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el nombe de la serie que quieres buscar: ");
        var nombresSerie = input.nextLine();
        Optional<Serie> serieBuscada = repository.findByTituloContainsIgnoreCase(nombresSerie);
        if (serieBuscada.isPresent()){
            System.out.println("La serie es: " + serieBuscada.get());
        }
        else{
            System.out.println("No encontrada");
        }
    }

    private void buscarTop5() {
        List<Serie> topSeries = repository.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s-> System.out.println("Serie: " + s.getTitulo() + ", Calificación: " + s.getEvaluacion()));
    }

    private void bucarPorGenero() {
        System.out.println("Escriba el género/categoría de la serie que quiere buscar");
        var genero = input.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesCategoria = repository.findByGenero(categoria);
        System.out.println("Estas son las series del género " + genero + ": ");
        seriesCategoria.forEach(System.out::println);
    }

    private void buscarPorTemporadayEvaluacion() {
        System.out.println("¿Con cuántas temporadas quieres que aparezcan las series?");
        var totalTemporadas = input.nextInt();
        input.nextLine();
        System.out.println("¿Con qué calificación queieres que aparezcan? ");
        var evaluacion = input.nextDouble();
        input.nextLine();
        List<Serie> filtroSeries = repository.seriesPorTemporadasYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("Las series son: ");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodiosTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = input.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
    }

        /*System.out.println("Escribe el nombre de la serie que deseas buscar");
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

        for (int i = 0; i < datos.totalTemporadas(); ++i) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); ++j) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo()))); /*Usa un lambda para iterar sobre cada elemento. Para cada temporada t, se ejecuta el método episodios, y por cada episodio de cada temporada se imprime su título

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

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());
        //episodios.forEach(System.out::println);

        /*System.out.println("Indica el año a partir del cual deseas ver los episodios:");
        var fecha = input.nextInt();
        input.nextLine();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        //LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
        episodios.stream()
                .filter(e -> e.getFechaLanzamiento() != null && e.getFechaLanzamiento().isAfter(fechaBusqueda))
                .forEach(e-> System.out.println(
                        "Temporada" + e.getTemporada() +
                                "Episodio " + e.getTitulo() +
                                " Fecha de Lanzammiento " + e.getFechaLanzamiento().format(dtf)
                ));
        System.out.println("Escribe el pedazo del titulo que deseas ver");
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
        }
        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media: " + est.getAverage());
        System.out.println("Episodio mejor evaluado: " + est.getMax());
        System.out.println("Episodio peor evaluado: " + est.getMin());*/
}