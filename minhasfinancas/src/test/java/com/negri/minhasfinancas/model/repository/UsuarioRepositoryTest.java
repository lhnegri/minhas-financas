package com.negri.minhasfinancas.model.repository;

import com.negri.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        //Cenário
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //Ação / Execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
        //Cenário

        //Ação / Execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados() {
        //cenario
        Usuario usuario = criarUsuario();

        //acao
        Usuario salvo = repository.save(usuario);

        //Verificacao
        Assertions.assertThat(usuario.getId()).isNotNull();
    }

    @Test
    public void deveRetornarUmUsuarioAoBuscarPorEmail() {
        //Cenario
        String email = "usuario@email.com";

        Usuario usuario = criarUsuario();
        Usuario usuarioSalvo = entityManager.persist(usuario);

        //Acao
        Optional<Usuario> result = repository.findByEmail(email);

        //Verificacao
        Assertions.assertThat(result.isPresent());
    }

    @Test
    public void deveRetornarVazioAoBuscarPorEmailQuandoNaoExistirNaBase() {
        //Cenario

        //Acao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");

        //Verificacao
        Assertions.assertThat(result.isPresent()).isFalse();
    }

    public static Usuario criarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha").build();
    }
}
