package br.com.luisedu.libraryapi.controller;

import br.com.luisedu.libraryapi.controller.dto.UsuarioDTO;
import br.com.luisedu.libraryapi.controller.mappers.UsuarioMapper;
import br.com.luisedu.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid  UsuarioDTO dto) {
        var usuario = mapper.toEntity(dto);

        service.salvar(usuario);
    }
}
