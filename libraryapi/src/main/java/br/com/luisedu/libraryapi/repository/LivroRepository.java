package br.com.luisedu.libraryapi.repository;

import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.model.GeneroLivro;
import br.com.luisedu.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @see LivroRepositoryTest
 */
public interface LivroRepository extends JpaRepository<Livro, UUID> {

    /// [JPA QUERY METHODS](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)
    // Query Method
    List<Livro> findByAutor(Autor autor);

    List<Livro> findByTitulo(String titulo);

    List<Livro> findByIsbn(String isbn);

    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    List<Livro> findByTituloOrPreco(String titulo, BigDecimal preco);

    List<Livro> findByTituloStartingWithIgnoreCaseOrderByTitulo(String titulo);

    // JPQL -> referencia as entidades e as propriedades
    @Query("select l from Livro as l order by l.titulo, l.preco")
    List<Livro> listarTodosOrderByTituloPreco();

    @Query("select a from Livro as l join l.autor a")
    List<Autor> listarAutoresDosLivros();

    @Query("""
                select l.genero
                from Livro l
                join l.autor a
                where a.nacionalidade = 'Brasileiro'
                order by l.genero
            """)
    List<String> listarGenerosAutoresBrasileiros();

    // named parameters -> parametros nomeados
    @Query("select l from Livro l where l.genero = :genero order by :paramOrdenacao")
    List<Livro> findByGenero(@Param("genero") GeneroLivro genero, @Param("paramOrdenacao") String paramOrdenacao);

    @Query("select l from Livro l where l.genero = ?1 order by ?2")
    List<Livro> findByGeneroPositionalParameters(GeneroLivro genero, String paramOrdenacao);

    @Transactional
    @Modifying
    @Query("delete from Livro where genero = ?1")
    void deleteByGenero(GeneroLivro genero);

    @Transactional
    @Modifying
    @Query("update Livro set dataPublicacao = ?1")
    void updateDatePublicacao(LocalDate novaDataPublicacao);
}
