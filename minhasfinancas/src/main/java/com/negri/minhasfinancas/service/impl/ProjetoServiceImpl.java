package com.negri.minhasfinancas.service.impl;

import com.negri.minhasfinancas.exception.RegraNegocioException;
import com.negri.minhasfinancas.model.entity.Projeto;
import com.negri.minhasfinancas.model.enums.StatusLancamento;
import com.negri.minhasfinancas.model.repository.LancamentoRepository;
import com.negri.minhasfinancas.model.repository.ProjetoRepository;
import com.negri.minhasfinancas.service.ProjetoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjetoServiceImpl implements ProjetoService {

    private ProjetoRepository repository;

    public ProjetoServiceImpl(ProjetoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Projeto salvar(Projeto projeto) {
        validar(projeto);
        return repository.save(projeto);
    }

    @Override
    @Transactional
    public Projeto atualizar(Projeto projeto) {
        Objects.requireNonNull(projeto.getId());
        validar(projeto);
        return repository.save(projeto);
    }

    @Override
    @Transactional
    public void deletar(Projeto projeto) {
        Objects.requireNonNull(projeto.getId());
        repository.delete(projeto);
    }

    @Override
    @Transactional
    public List<Projeto> buscar(Projeto projetoFiltro) {
        Example example = Example.of( projetoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) );

        return repository.findAll(example);
    }

    @Override
    public void validar(Projeto projeto) {
        if(projeto.getNomeCliente() == null || projeto.getNomeCliente().trim().equals("")) {
            throw new RegraNegocioException("Informe o Nome do Cliente.");
        }

        if(projeto.getDescricaoProjeto() == null) {
            throw new RegraNegocioException("Informe a Descrição do Projeto.");
        }

        if(projeto.getDataServico() == null) {
            throw new RegraNegocioException("Informe a Data do Serviço.");
        }

        if(projeto.getValorServico() == null || projeto.getValorServico().compareTo(BigDecimal.ZERO) < 1 ) {
            throw new RegraNegocioException("Informe o Valor do Serviço.");
        }
    }

    @Override
    public Optional<Projeto> obterPorId(Long id) {
        return repository.findById(id);
    }
}
