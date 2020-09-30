package com.negri.minhasfinancas.service.impl;

import com.negri.minhasfinancas.exception.ErroAutenticacaoException;
import com.negri.minhasfinancas.exception.RegraNegocioException;
import com.negri.minhasfinancas.model.entity.Usuario;
import com.negri.minhasfinancas.model.repository.UsuarioRepository;
import com.negri.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private UsuarioRepository repository;

    public UsuarioServiceImpl (UsuarioRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if(!usuario.isPresent()) {
            throw new ErroAutenticacaoException("Nenhum usuário encontrado para o Email informado.");
        }

        if(!usuario.get().getSenha().equals(senha)) {
            throw new ErroAutenticacaoException("Senha inválida.");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);

        if(existe) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com esse email.");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return repository.findById(id);
    }
}
