package es.safareyes.smartchefproject.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "preferencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private TipoPreferencia tipo;
}