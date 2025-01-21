package br.com.luisedu.libraryapi.controller;

import br.com.luisedu.libraryapi.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String paginaHome(Authentication auth) {
        if (auth instanceof CustomAuthentication customAuth) {
            System.out.println(customAuth.getUsuario());
        }

        return "Olá " + auth.getName();
    }
}
