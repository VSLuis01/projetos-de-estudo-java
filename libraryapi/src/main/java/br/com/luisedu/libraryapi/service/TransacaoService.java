package br.com.luisedu.libraryapi.service;

import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.model.GeneroLivro;
import br.com.luisedu.libraryapi.model.Livro;
import br.com.luisedu.libraryapi.repository.AutorRepository;
import br.com.luisedu.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void salvarLivroComFoto(Livro livro) {
        // salva o livro
        // repository.save(livro)

        // pega o id do livro = livro.getId();
        // var id = livro.getId();

        // salvar foto do livro -> bucket na nuvem
        // bucketService.salvar(livro.getFoto(), id + ".png");

        // atualizar o nome arquivo que foi salvo
        // livro.setNomeArquivoFoto(id + ".png");
    }

    @Transactional
    public void atualizacaoSemAtualizar() {
        var livro = livroRepository.findById(UUID.fromString("858f7a9a-78b2-4a91-9866-52adce677e27")).orElse(null);

        livro.setDataPublicacao(LocalDate.now());

//        livroRepository.save(livro);
    }

    @Transactional
    public void executar() {
        // salva autor
        Autor autor = new Autor();

        autor.setNome("TESTE Francisco");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(2002, 10, 9));

//        this.autorRepository.saveAndFlush(autor); Esta operação já manda direto para o banco. Não espera o fim da transação.
        this.autorRepository.save(autor);

        // Salva o livro
        Livro livro = new Livro();
        livro.setIsbn("13123-21313");
        livro.setPreco(BigDecimal.valueOf(100));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("TESTE NOVO LIVRO 2");
        livro.setDataPublicacao(LocalDate.of(2020, 1, 1));

        livro.setAutor(autor);

        this.livroRepository.save(livro);

        if (autor.getNome().equals("TESTE Francisco")) {
            throw new RuntimeException("Rollback!");
        }
    }
}
