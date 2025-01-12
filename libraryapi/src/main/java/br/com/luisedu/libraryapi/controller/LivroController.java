package br.com.luisedu.libraryapi.controller;

import br.com.luisedu.libraryapi.controller.dto.CadastroLivroDTO;
import br.com.luisedu.libraryapi.controller.dto.ErroResposta;
import br.com.luisedu.libraryapi.controller.mappers.LivroMapper;
import br.com.luisedu.libraryapi.exceptions.RegistroDuplicadoException;
import br.com.luisedu.libraryapi.model.Livro;
import br.com.luisedu.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;
    private final LivroMapper mapper;


    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        try {
            Livro livro = mapper.toEntity(dto);
            livroService.salver(livro);
            // criar a url para acesso dos dados do livro
            // retornar o código created com header location
            return ResponseEntity.ok(livro);
        } catch (RegistroDuplicadoException e) {
            var erroDto = ErroResposta.conflito(e.getMessage());

            return ResponseEntity.status(erroDto.status()).body(erroDto);
        }
    }
}
