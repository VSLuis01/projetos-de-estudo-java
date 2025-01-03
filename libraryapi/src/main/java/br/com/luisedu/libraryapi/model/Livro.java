package br.com.luisedu.libraryapi.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "livro", schema = "public")
@Data

/*
@Data é tudo isso

@Getter@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor // atributos com final
@NoArgsConstructor
@AllArgsConstructor*/
@ToString(exclude = "autor")
public class Livro {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(name = "preco", precision = 18, scale = 2)
    private BigDecimal preco;

    /**
     * CASCADE TYPES:
     * ALL - TODAS
     * PERSIST - Vai criar todas as entidades que está relacionado ao livro. No caso AUTOR
     * MERGE -
     */
    @JoinColumn(name = "id_autor")
    @ManyToOne(fetch = FetchType.LAZY)
    private Autor autor;
}
