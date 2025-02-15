package br.com.luisedu.libraryapi.service;

import br.com.luisedu.libraryapi.model.GeneroLivro;
import br.com.luisedu.libraryapi.model.Livro;
import br.com.luisedu.libraryapi.repository.LivroRepository;
import br.com.luisedu.libraryapi.security.SecurityService;
import br.com.luisedu.libraryapi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static br.com.luisedu.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;
    private final SecurityService securityService;

    private final LivroValidator validator;

    public void salvar(Livro livro) {
        livro.setIsbn(livro.getIsbn().replaceAll("\\D", ""));

        validator.validar(livro);

        livro.setUsuario(securityService.obterUsuarioAutenticado());

        livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id) {
        return livroRepository.findById(id);
    }

    public void deletar(Livro livro) {
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisa(String isbn,
                                String titulo,
                                String nomeAutor,
                                GeneroLivro genero,
                                Integer anoPublicacao,
                                Integer pagina,
                                Integer tamanhoPagina) {

        // select * from livro where isbn = :isbn and nomeAutor =

/*        Specification<Livro> specs = Specification
                .where(LivroSpecs.isbnEqual(isbn))
                .and(LivroSpecs.tituloLike(titulo))
                .and(LivroSpecs.generoEqual(genero));*/

        Specification<Livro> specs = Specification
                .where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            // query = query and isbn = :isbn
            specs = specs.and(isbnEqual(isbn));
        }

        if (titulo != null) {
            specs = specs.and(tituloLike(titulo));
        }

        if (genero != null) {
            specs = specs.and(generoEqual(genero));
        }

        if (anoPublicacao != null) {
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }

        if (nomeAutor != null) {
            specs = specs.and(nomeAutorLike(nomeAutor));
        }
        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs, pageRequest);

    }

    public void atualizar(Livro livro) {
        livro.setIsbn(livro.getIsbn().replaceAll("\\D", ""));

        if (livro.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessário que o livro ja esteja salvo na base");
        }

        validator.validar(livro);

        livroRepository.save(livro);
    }
}
