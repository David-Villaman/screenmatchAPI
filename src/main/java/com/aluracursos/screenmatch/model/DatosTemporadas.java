package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora propiedades desconocidas en el JSON

public record DatosTemporadas(
        @JsonAlias("Season") Integer numero,
        @JsonAlias("Episodes") List<DatosEpisodio> episodios
) {
}
