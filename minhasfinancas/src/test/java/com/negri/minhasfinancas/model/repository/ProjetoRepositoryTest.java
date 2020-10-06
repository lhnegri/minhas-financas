package com.negri.minhasfinancas.model.repository;

import com.negri.minhasfinancas.model.entity.Projeto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProjetoRepositoryTest {

    @Autowired
    ProjetoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmProjeto() {
        Projeto projeto = criarProjeto();

        projeto = repository.save(projeto);

        assertThat(projeto.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmProjeto() {
        Projeto projeto = criarEPersistirUmProjeto();

        projeto = entityManager.find(Projeto.class, projeto.getId());

        repository.delete(projeto);

        Projeto projetoInexistente = entityManager.find(Projeto.class, projeto.getId());
        assertThat(projetoInexistente).isNull();
    }


    @Test
    public void deveAtualizarUmProjeto() {
        Projeto projeto = criarEPersistirUmProjeto();

        projeto.setNomeCliente("Novo Cliente");
        projeto.setDescricaoProjeto("Novo Projeto");

        repository.save(projeto);

        Projeto projetoAtualizado = entityManager.find(Projeto.class, projeto.getId());

        assertThat(projetoAtualizado.getNomeCliente()).isEqualTo("Novo Cliente");
        assertThat(projetoAtualizado.getDescricaoProjeto()).isEqualTo("Novo Projeto");
    }

    @Test
    public void deveBuscarUmProjetoPorId() {
        Projeto projeto = criarEPersistirUmProjeto();

        Optional<Projeto> projetoEncontrado = repository.findById(projeto.getId());

        assertThat(projetoEncontrado.isPresent()).isTrue();
    }

    private Projeto criarEPersistirUmProjeto() {
        Projeto projeto = criarProjeto();
        entityManager.persist(projeto);
        return projeto;
    }

    public static Projeto criarProjeto() {
        return Projeto.builder()
                .nomeCliente("Cliente")
                .descricaoProjeto("Projeto")
                .dataServico(LocalDate.now())
                .valorServico(BigDecimal.valueOf(10000))
                .dataInicioPagamento(LocalDate.now())
                .dataInicioRecebimento(LocalDate.now())
                .numeroParcelasPagamento(5)
                .numeroParcelasRecebimento(5)
                .build();
    }
}
