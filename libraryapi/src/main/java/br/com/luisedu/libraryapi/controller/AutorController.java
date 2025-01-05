package br.com.luisedu.libraryapi.controller;


import br.com.luisedu.libraryapi.controller.dto.AutorDTO;
import br.com.luisedu.libraryapi.controller.dto.ErroResposta;
import br.com.luisedu.libraryapi.exceptions.OperacaoNaoPermitidaExpection;
import br.com.luisedu.libraryapi.exceptions.RegistroDuplicadoException;
import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
// baseUrl: http://localhost:8080/autores
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autorDTO) {
        try {

            var autorEntidade = autorDTO.toAutor();
            autorService.salvar(autorEntidade);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorEntidade.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());

            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(@PathVariable("id") String id, @RequestBody @Valid AutorDTO autorDTO) {
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.buscarPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = autorOptional.get();

            autor.setNome(autorDTO.nome());
            autor.setNacionalidade(autorDTO.nacionalidade());
            autor.setDataNascimento(autorDTO.dataNascimento());

            autorService.atualizar(autor);

            return ResponseEntity.noContent().build();
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.conflito(e.getMessage());

            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> findById(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);

        Optional<Autor> autorOptional = autorService.buscarPorId(idAutor);

        if (autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(autor.getId(), autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());

            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> remover(@PathVariable("id") String id) {
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.buscarPorId(idAutor);

            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            autorService.remover(autorOptional.get());
            return ResponseEntity.noContent().build();
        } catch (OperacaoNaoPermitidaExpection e) {
            var erroResposta = ErroResposta.respostaPadrao(e.getMessage());

            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> resultado = autorService.pesquisaByExample(nome, nacionalidade);

        List<AutorDTO> dtos = resultado.stream().map(Autor::toAutorDTO).toList();

        return ResponseEntity.ok(dtos);
    }
}
