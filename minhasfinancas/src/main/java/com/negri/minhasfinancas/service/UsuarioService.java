package com.negri.minhasfinancas.service;

import com.negri.minhasfinancas.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar (String email, String senha);

    Usuario salvarUsuario (Usuario usuario);

    void validarEmail (String email);

    Optional<Usuario> obterPorId(Long id);
}
