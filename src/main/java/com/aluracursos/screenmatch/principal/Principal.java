package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.services.ConsumoAPI;
import com.aluracursos.screenmatch.services.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=77cfba0e";
    private Scanner teclado = new Scanner(System.in);
    ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu() {
        System.out.println("Bienvenido a ScreenMatch");
        System.out.println("Escribe el nombre de la serie que deseas buscar:");
        // Buscar datos de una serie
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println("\n*** Datos de la serie " + nombreSerie + " ***");
        System.out.println(datos);

        // Obtener datos de todas las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            DatosTemporadas temporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(temporada);
        }
        System.out.println("\n*** Datos de todas las temporadas de " + nombreSerie + " ***");
        //temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de cada episodio
//        for (int i = 0; i < datos.totalTemporadas(); i++) {
//            List<DatosEpisodio> episodios = temporadas.get(i).episodios();
//            for (int j = 0; i < episodios.size(); i++) {
//                System.out.println(episodios.get(j).titulo());
//            }
//        }
       // temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir toda la informacion a una lista del tipo DatosEpisodio

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //Top 5 episodios con mejor calificación
        System.out.println("\n*** Top 5 episodios con mejor calificación ***");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);


        System.out.println("\n*** Episodios ordenados por número de temporada y episodio ***");
        //Convirtiendo los dato a una lista de tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d ->new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //Busqueda de episodios a partir de un año

        System.out.println("Inidica el año a partir del cual deseas buscar los episodios:");
        var fecha = teclado.nextInt();
        teclado.nextLine();

        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println("Temporada: " + e.getTemporada() + ", Título: " + e.getTitulo() +
                        ", Fecha de lanzamiento: " + e.getFechaDeLanzamiento().format(formatter)));

    }
}