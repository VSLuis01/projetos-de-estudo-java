package br.com.luisedu.libraryapi.security;


import br.com.luisedu.libraryapi.model.Usuario;
import br.com.luisedu.libraryapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private final UsuarioService usuarioService;

    public Usuario obterUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return usuarioService.obterPorLogin(userDetails.getUsername());
    }
}
