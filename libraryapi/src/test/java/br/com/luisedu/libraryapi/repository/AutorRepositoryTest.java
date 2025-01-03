package br.com.luisedu.libraryapi.repository;


import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.model.GeneroLivro;
import br.com.luisedu.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest() {
        Autor autor = new Autor();

        autor.setNome("Luis Test");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(2002, 10, 9));

        var autorSalvo = autorRepository.save(autor);

        System.out.println("Autor Salvo: " + autorSalvo);
    }

    @Test
    public void atualizarTest() {
        UUID autorId = UUID.fromString("8159e952-57af-4c9d-9fa3-9655ebbb11bf");

        Optional<Autor> possivelAutor = autorRepository.findById(autorId);

        if (possivelAutor.isPresent()) {
            Autor autor = possivelAutor.get();
            System.out.println("Dados do Autor: " + possivelAutor.get());

            autor.setNome("Luis Test ATUALIZADO");

            autorRepository.save(autor);
        }
    }

    @Test
    public void listarTest() {
        List<Autor> autores = this.autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    @Test
    public void countTest() {
        System.out.println("Contagem de autores: " + this.autorRepository.count());
    }

    @Test
    public void deleteByIdTest() {
        UUID autorId = UUID.fromString("8159e952-57af-4c9d-9fa3-9655ebbb11bf");
        this.autorRepository.deleteById(autorId);
    }

    @Test
    public void deleteTest() {
        UUID autorId = UUID.fromString("7c8afc0f-8c99-4e82-845c-ce8323518107");

        Autor autor = this.autorRepository.findById(autorId).orElse(null);

        this.autorRepository.delete(autor);
    }

    @Test
    void salvarAutorComLivrosTest() {
        Autor autor = new Autor();

        autor.setNome("antonio");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(2001, 10, 9));

        Livro livro = new Livro();
        livro.setIsbn("13123-21313");
        livro.setPreco(BigDecimal.valueOf(222));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setTitulo("LIVRO MISTERIOSO");
        livro.setDataPublicacao(LocalDate.of(1986, 1, 1));
        livro.setAutor(autor);

        Livro livro2 = new Livro();
        livro2.setIsbn("1232-21313");
        livro2.setPreco(BigDecimal.valueOf(222));
        livro2.setGenero(GeneroLivro.CIENCIA);
        livro2.setTitulo("LIVRO CIENCIA");
        livro2.setDataPublicacao(LocalDate.of(1986, 1, 1));
        livro2.setAutor(autor);

        List<Livro> livroList = List.of(livro, livro2);

        autor.setLivros(livroList);

        this.autorRepository.save(autor);

//        this.livroRepository.saveAll(livroList);
    }

    @Test
    @Transactional
    void listarLivrosAutorTest() {
        UUID autorId = UUID.fromString("73e90da2-4a69-4b18-ac67-3b57d9647007");
        Autor autor = this.autorRepository.findById(autorId).orElse(null);

        // bucas os livros do autor
        List<Livro> livroList = this.livroRepository.findByAutor(autor);
        autor.setLivros(livroList);

        autor.getLivros().forEach(System.out::println);
    }
}
