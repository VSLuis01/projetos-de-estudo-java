package br.com.luisedu.libraryapi.controller.mappers;

import br.com.luisedu.libraryapi.controller.dto.UsuarioDTO;
import br.com.luisedu.libraryapi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
