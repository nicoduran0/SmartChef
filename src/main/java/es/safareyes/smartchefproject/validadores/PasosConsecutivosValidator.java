package es.safareyes.smartchefproject.validadores;

import es.safareyes.smartchefproject.dtos.request.PasoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Objects;

public class PasosConsecutivosValidator implements ConstraintValidator<PasosConsecutivos, List<PasoRequest>> {

    @Override
    public boolean isValid(List<PasoRequest> pasos, ConstraintValidatorContext contexto) {
        if (pasos == null || pasos.isEmpty()) {
            return true; // de eso ya se encarga @NotEmpty
        }
        List<Integer> numeros = pasos.stream()
                .map(PasoRequest::numero)
                .filter(Objects::nonNull)
                .map(Short::intValue)
                .sorted()
                .toList();
        if (numeros.size() != pasos.size()) {
            return true; // los números nulos los marca @NotNull
        }
        for (int i = 0; i < numeros.size(); i++) {
            if (numeros.get(i) != i + 1) {
                return false;
            }
        }
        return true;
    }
}