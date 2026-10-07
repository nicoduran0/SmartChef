package es.safareyes.smartchefproject.modelos;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"contrasena", "preferencias"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol = Rol.USUARIO;

    @CreatedDate
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @LastModifiedDate
    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuarios_preferencias",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "preferencia_id")
    )
    private Set<Preferencia> preferencias = new HashSet<>();
}