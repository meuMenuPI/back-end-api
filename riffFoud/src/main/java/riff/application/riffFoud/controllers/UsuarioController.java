package riff.application.riffFoud.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riff.application.riffFoud.usuario.DadosCadastroUsuario;
import riff.application.riffFoud.usuario.Usuario;
import riff.application.riffFoud.usuario.UsuarioRepository;

@RestController
@RequestMapping("/riff/usuario")
public class UsuarioController {
@Autowired
    private UsuarioRepository repository;

    @PostMapping("/cadastrar")
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroUsuario dados){

        repository.save(new Usuario(dados));

    }

}
