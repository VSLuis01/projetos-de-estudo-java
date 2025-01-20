package br.com.luisedu.libraryapi.service;

import br.com.luisedu.libraryapi.exceptions.OperacaoNaoPermitidaExpection;
import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.repository.AutorRepository;
import br.com.luisedu.libraryapi.repository.LivroRepository;
import br.com.luisedu.libraryapi.security.SecurityService;
import br.com.luisedu.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;
    private final SecurityService securityService;

    public Autor salvar(Autor autor) {
        validator.validar(autor);

        autor.setUsuario(securityService.obterUsuarioAutenticado());

        return autorRepository.save(autor);
    }

    public void atualizar(Autor autor) {
        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessário que o autor ja esteja salvo na base");
        }

        validator.validar(autor);

        autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id) {
        return autorRepository.findById(id);
    }

    public void remover(Autor autor) {
        if (possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaExpection("Não é permitido excluir um Autor que possui livros cadastrados");
        }

        autorRepository.delete(autor);
    }

    public List<Autor> pesquisar(String nome, String nacionalidade) {
        if (nome != null && nacionalidade != null) {
            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);
        }

        if (nome != null) {
            return autorRepository.findByNome(nome);
        }

        if (nacionalidade != null) {
            return autorRepository.findByNacionalidade(nacionalidade);
        }

        return autorRepository.findAll();
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade) {
        var autor = new Autor();

        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "dataNascimento")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> autorExample = Example.of(autor, matcher);

        return autorRepository.findAll(autorExample);
    }

    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }
}
