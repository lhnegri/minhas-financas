package com.negri.minhasfinancas.service;

import com.negri.minhasfinancas.exception.ErroAutenticacaoException;
import com.negri.minhasfinancas.exception.RegraNegocioException;
import com.negri.minhasfinancas.model.entity.Usuario;
import com.negri.minhasfinancas.model.repository.UsuarioRepository;
import com.negri.minhasfinancas.service.impl.UsuarioServiceImpl;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    UsuarioService service;

    @MockBean
    UsuarioRepository repository;

    @BeforeEach
    public void setUp() {
        service = new UsuarioServiceImpl(repository);
    }

    @Test
    public void deveValidarEmail() {
        //Cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //acao
        service.validarEmail("email@gmail.com");

        //verificacao

    }

    @Test
    public void deveLancarExcessaoAoValidarEmailQuandoExistirEmailCadastrado() {
        //Cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //acao - verificacao
        org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> service.validarEmail("usuario@email.com"));
    }

    @Test
    public void deveLancarExcessaoQuandoNaoEncontrarUmUsuarioPorEmail() {
        //Cenario
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //acao - verificacao
        Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar("usuario@email.com", "senha"));
        org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Nenhum usuário encontrado para o Email informado.");
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        //Cenario
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario
                .builder()
                .email(email)
                .senha(senha)
                .id(1L)
                .build();

        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        //Acao e Verificacao
        org.assertj.core.api.Assertions.assertThat(service.autenticar(email, senha)).isNotNull();
    }

    @Test
    public void deveLancarExcessaoAoAutenticarPorFalhaDeSenhaNaoConfere() {
        //Cenario
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario
                .builder()
                .email(email)
                .senha(senha)
                .id(1L)
                .build();

        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        //Acao e verificacao
        String senhaAux = "senha2";

        Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar(email, senhaAux));
        org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacaoException.class).hasMessage("Senha inválida.");
    }
}