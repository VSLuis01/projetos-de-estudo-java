package br.com.luisedu.libraryapi.service;

import br.com.luisedu.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;
}
