package es.safareyes.smartchefproject.controladores;

import es.safareyes.smartchefproject.dtos.response.RecetasPopularesDto;
import es.safareyes.smartchefproject.servicios.IInformeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/informes")
@RequiredArgsConstructor
public class InformeController {

    private final IInformeService informeService;

    // TODO seguridad: solo ADMIN y CHEF
    @GetMapping("/recetas-populares")
    public RecetasPopularesDto recetasPopulares(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return informeService.recetasPopulares(desde, hasta);
    }
}