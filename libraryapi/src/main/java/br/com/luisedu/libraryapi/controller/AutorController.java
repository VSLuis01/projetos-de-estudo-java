package br.com.luisedu.libraryapi.controller;


import br.com.luisedu.libraryapi.controller.dto.AutorDTO;
import br.com.luisedu.libraryapi.controller.mappers.AutorMapper;
import br.com.luisedu.libraryapi.model.Autor;
import br.com.luisedu.libraryapi.model.Usuario;
import br.com.luisedu.libraryapi.security.SecurityService;
import br.com.luisedu.libraryapi.service.AutorService;
import br.com.luisedu.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
// baseUrl: http://localhost:8080/autores
public class AutorController implements GenericController {

    private final AutorService autorService;
    private final AutorMapper mapper;

    @PostMapping
    //@RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> salvar(@RequestBody @Valid AutorDTO dto/*, Authentication auth*/) {
        /* POSSÍVEL FORMA. INJETANDO NO MÉTODO
        System.out.println(auth);

        UserDetails usuarioAutenticado = (UserDetails) auth.getPrincipal();
        Usuario usuario = usuarioService.obterPorLogin(usuarioAutenticado.getUsername());*/

        var autor = mapper.toEntity(dto);
//        autor.setIdUsuario(usuario.getId());
        autorService.salvar(autor);

        URI location = gerarHeaderLocation(autor.getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> atualizar(@PathVariable("id") String id, @RequestBody @Valid AutorDTO autorDTO) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var autor = autorOptional.get();

        autor.setNome(autorDTO.nome());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autor.setDataNascimento(autorDTO.dataNascimento());

        autorService.atualizar(autor);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<AutorDTO> findById(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);

        return autorService
                .obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDto(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> remover(@PathVariable("id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        autorService.remover(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> resultado = autorService.pesquisaByExample(nome, nacionalidade);

        List<AutorDTO> dtos = resultado
                .stream()
                .map(mapper::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
