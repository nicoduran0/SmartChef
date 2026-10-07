package es.safareyes.smartchefproject.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ingredientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private UnidadMedida unidad;

    @Column(nullable = false, precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal calorias = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 4)
    @Builder.Default
    private BigDecimal precio = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean vegetariano;

    @Column(name = "sin_gluten", nullable = false)
    private boolean sinGluten;
}