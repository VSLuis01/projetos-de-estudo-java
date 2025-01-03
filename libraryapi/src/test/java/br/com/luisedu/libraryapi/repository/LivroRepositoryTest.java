package br.com.luisedu.libraryapi.repository;

import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.model.GeneroLivro;
import br.com.luisedu.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Test
    void salvarTest() {
        Livro livro = new Livro();
        livro.setIsbn("13123-21313");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setTitulo("CIENENCIAS");
        livro.setDataPublicacao(LocalDate.of(2020, 1, 1));

        Autor autor = autorRepository
                .findById(UUID.fromString("2056816c-a5e2-432d-b1b5-9f6e7e3de3e4"))
                .orElse(null);

//        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void salvarCascadeTest() {
        Livro livro = new Livro();
        livro.setIsbn("13123-21313");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Livro CASDADE");
        livro.setDataPublicacao(LocalDate.of(2020, 1, 1));

        Autor autor = new Autor();

        autor.setNome("Luis CASCADE");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(2002, 10, 9));

        livro.setAutor(autor);

        this.livroRepository.save(livro);
    }

    @Test
    void atualizarAutorLivroTest() {
        UUID id = UUID.fromString("ac2d0687-1d31-4881-846c-860355f6867f");

        Livro livro = this.livroRepository.findById(id).orElse(null);

        Autor autor = autorRepository.findById(UUID.fromString("0e751cdd-30f1-46c3-83d6-f7377f5a1139")).orElse(null);

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    void deleteById() {
        UUID id = UUID.fromString("ac2d0687-1d31-4881-846c-860355f6867f");

        this.livroRepository.deleteById(id);
    }

    @Test
    @Transactional // utilizado para carregamentos LAZY
    void findById() {
        UUID id = UUID.fromString("858f7a9a-78b2-4a91-9866-52adce677e27");

        Livro livro = this.livroRepository.findById(id).orElse(null);

        System.out.println("Livro: ");
        System.out.println(livro.getTitulo());

        System.out.println("Autor: ");
        System.out.println(livro.getAutor().getNome());

    }

    @Test
    void findByTitulo() {
        List<Livro> livros = livroRepository.findByTitulo("LIVRO MISTERIOSO");

        livros.forEach(System.out::println);
    }

    @Test
    void findByIsbn() {
        List<Livro> byIsbn = livroRepository.findByIsbn("13123-21313");

        byIsbn.forEach(System.out::println);
    }

    @Test
    void findByTituloOrPreco() {
        List<Livro> byIsbn = livroRepository.findByTituloOrPreco("LIVRO MISTERIOSO", BigDecimal.valueOf(100));

        byIsbn.forEach(System.out::println);
    }

    @Test
    void findByTituloStartingWithIgnoreCaseOrderByTitulo() {
        List<Livro> livroDe = livroRepository.findByTituloStartingWithIgnoreCaseOrderByTitulo("livro");

        livroDe.forEach(System.out::println);
    }

    @Test
    void listarLivrosComQueryJPQL() {
        List<Livro> livroList = livroRepository.listarTodosOrderByTituloPreco();
        livroList.forEach(System.out::println);
    }

    @Test
    void listarAutoresDosLivros() {
        List<Autor> autors = livroRepository.listarAutoresDosLivros();

        autors.forEach(System.out::println);
    }

    @Test
    void listarGenerosAutoresBrasileiros() {
        List<String> strings = livroRepository.listarGenerosAutoresBrasileiros();
        strings.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParamTest() {
        List<Livro> livros = livroRepository.findByGenero(GeneroLivro.MISTERIO, "dataPublicacao");
        livros.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroPositionalParametersTest() {
        List<Livro> livros = livroRepository.findByGeneroPositionalParameters(GeneroLivro.MISTERIO, "dataPublicacao");
        livros.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTest() {
        this.livroRepository.deleteByGenero(GeneroLivro.CIENCIA);
    }

    @Test
    void atualizarDataPublicacao() {
        this.livroRepository.updateDatePublicacao(LocalDate.of(2002, 10, 9));
    }
}