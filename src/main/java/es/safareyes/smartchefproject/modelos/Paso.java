package es.safareyes.smartchefproject.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pasos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "receta")
public class Paso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receta_id", nullable = false)
    private Receta receta;

    @Column(nullable = false)
    private Short numero;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
}