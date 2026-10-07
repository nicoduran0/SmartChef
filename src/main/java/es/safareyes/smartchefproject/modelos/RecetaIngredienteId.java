package es.safareyes.smartchefproject.modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RecetaIngredienteId implements Serializable {

    @Column(name = "receta_id")
    private Long recetaId;

    @Column(name = "ingrediente_id")
    private Long ingredienteId;
}