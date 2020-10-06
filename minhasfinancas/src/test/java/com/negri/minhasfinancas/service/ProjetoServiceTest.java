package com.negri.minhasfinancas.service;

import com.negri.minhasfinancas.exception.RegraNegocioException;
import com.negri.minhasfinancas.model.entity.Projeto;
import com.negri.minhasfinancas.model.repository.ProjetoRepository;
import com.negri.minhasfinancas.model.repository.ProjetoRepositoryTest;
import com.negri.minhasfinancas.service.impl.ProjetoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProjetoServiceTest {

    @SpyBean
    ProjetoServiceImpl service;

    @MockBean
    ProjetoRepository repository;

    @Test
    public void deveSalvarUmProjeto() {
        //cenário
        Projeto projetoASalvar = ProjetoRepositoryTest.criarProjeto();
        doNothing().when(service).validar(projetoASalvar);

        Projeto projetoSalvo = ProjetoRepositoryTest.criarProjeto();
        projetoSalvo.setId(1l);
        when(repository.save(projetoASalvar)).thenReturn(projetoSalvo);

        //execucao
        Projeto projeto = service.salvar(projetoASalvar);

        //verificação
        assertThat( projeto.getId() ).isEqualTo(projetoSalvo.getId());
    }

    @Test
    public void naoDeveSalvarUmProjetoQuandoHouverErroDeValidacao() {
        //cenário
        Projeto projetoASalvar = ProjetoRepositoryTest.criarProjeto();
        doThrow( RegraNegocioException.class ).when(service).validar(projetoASalvar);

        //execucao e verificacao
        catchThrowableOfType( () -> service.salvar(projetoASalvar), RegraNegocioException.class );
        verify(repository, never()).save(projetoASalvar);
    }

    @Test
    public void deveAtualizarUmProjeto() {
        //cenário
        Projeto projetoSalvo = ProjetoRepositoryTest.criarProjeto();
        projetoSalvo.setId(1l);
        projetoSalvo.setNomeCliente("Nome Cliente novo");

        doNothing().when(service).validar(projetoSalvo);

        when(repository.save(projetoSalvo)).thenReturn(projetoSalvo);

        //execucao
        service.atualizar(projetoSalvo);

        //verificação
        verify(repository, times(1)).save(projetoSalvo);

    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmProjetoQueAindaNaoFoiSalvo() {
        //cenário
        Projeto projeto = ProjetoRepositoryTest.criarProjeto();

        //execucao e verificacao
        catchThrowableOfType( () -> service.atualizar(projeto), NullPointerException.class );
        verify(repository, never()).save(projeto);
    }

    @Test
    public void deveDeletarUmProjeto() {
        //cenário
        Projeto projeto = ProjetoRepositoryTest.criarProjeto();
        projeto.setId(1l);

        //execucao
        service.deletar(projeto);

        //verificacao
        verify( repository ).delete(projeto);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmProjetoQueAindaNaoFoiSalvo() {

        //cenário
        Projeto projeto = ProjetoRepositoryTest.criarProjeto();

        //execucao
        catchThrowableOfType( () -> service.deletar(projeto), NullPointerException.class );

        //verificacao
        verify( repository, never() ).delete(projeto);
    }


    @Test
    public void deveFiltrarProjetos() {
        //cenário
        Projeto projeto = ProjetoRepositoryTest.criarProjeto();
        projeto.setId(1l);

        List<Projeto> lista = Arrays.asList(projeto);
        when( repository.findAll(any(Example.class)) ).thenReturn(lista);

        //execucao
        List<Projeto> resultado = service.buscar(projeto);

        //verificacoes
        assertThat(resultado)
                .isNotEmpty()
                .hasSize(1)
                .contains(projeto);

    }

    @Test
    public void deveObterUmProjetoPorID() {
        //cenário
        Long id = 1l;

        Projeto projeto = ProjetoRepositoryTest.criarProjeto();
        projeto.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(projeto));

        //execucao
        Optional<Projeto> resultado =  service.obterPorId(id);

        //verificacao
        assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    public void deveREtornarVazioQuandoOProjetoNaoExiste() {
        //cenário
        Long id = 1l;

        Projeto projeto = ProjetoRepositoryTest.criarProjeto();
        projeto.setId(id);

        when( repository.findById(id) ).thenReturn( Optional.empty() );

        //execucao
        Optional<Projeto> resultado =  service.obterPorId(id);

        //verificacao
        assertThat(resultado.isPresent()).isFalse();
    }

    @Test
    public void deveLancarErrosAoValidarUmProjeto() {
        Projeto projeto = new Projeto();

        Throwable erro = Assertions.catchThrowable( () -> service.validar(projeto) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe o Nome do Cliente.");

        projeto.setNomeCliente("");

        erro = Assertions.catchThrowable( () -> service.validar(projeto) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe o Nome do Cliente.");

        projeto.setNomeCliente("Nome Cliente");

        erro = Assertions.catchThrowable( () -> service.validar(projeto) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe a Descrição do Projeto.");

        projeto.setDescricaoProjeto("");

        erro = catchThrowable( () -> service.validar(projeto) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe a Descrição do Projeto.");

        projeto.setDescricaoProjeto("Descrição do projeto");

        erro = catchThrowable( () -> service.validar(projeto) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe a Data do Serviço.");

        projeto.setDataServico(LocalDate.now());

        erro = catchThrowable( () -> service.validar(projeto) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe o Valor do Serviço.");

        projeto.setValorServico(new BigDecimal(-2));

        erro = catchThrowable( () -> service.validar(projeto) );
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor de Serviço válido.");
    }
}