package br.com.luisedu.libraryapi.controller.mappers;

import br.com.luisedu.libraryapi.controller.dto.AutorDTO;
import br.com.luisedu.libraryapi.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    Autor toEntity(AutorDTO autorDTO);

    AutorDTO toDto(Autor autor);
}
