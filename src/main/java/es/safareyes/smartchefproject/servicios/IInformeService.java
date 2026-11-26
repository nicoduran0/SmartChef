package es.safareyes.smartchefproject.servicios;

import es.safareyes.smartchefproject.dtos.response.RecetasPopularesDto;

import java.time.LocalDate;

public interface IInformeService {

    RecetasPopularesDto recetasPopulares(LocalDate desde, LocalDate hasta);
}