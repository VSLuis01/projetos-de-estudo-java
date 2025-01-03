package br.com.luisedu.libraryapi.repository;

import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.model.GeneroLivro;
import br.com.luisedu.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
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


    List<Livro> findByGenero(GeneroLivro genero);
}
