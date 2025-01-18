package br.com.luisedu.libraryapi.controller;

import br.com.luisedu.libraryapi.controller.dto.CadastroLivroDTO;
import br.com.luisedu.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import br.com.luisedu.libraryapi.controller.mappers.LivroMapper;
import br.com.luisedu.libraryapi.model.GeneroLivro;
import br.com.luisedu.libraryapi.model.Livro;
import br.com.luisedu.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GenericController {

    private final LivroService livroService;
    private final LivroMapper mapper;


    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        livroService.salver(livro);

        var url = gerarHeaderLocation(livro.getId());

        return ResponseEntity.created(url).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id) {
        return livroService
                .obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = mapper.toDTO(livro);

                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletar(@PathVariable("id") String id) {
        return livroService
                .obterPorId(UUID.fromString(id))
                .map(livro -> {
                    livroService.deletar(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao) {
        var resultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao);

        List<ResultadoPesquisaLivroDTO> list = resultado.stream().map(mapper::toDTO).toList();

        return ResponseEntity.ok(list);
    }
}
