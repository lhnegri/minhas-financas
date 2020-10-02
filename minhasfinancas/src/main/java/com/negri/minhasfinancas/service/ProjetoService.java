package com.negri.minhasfinancas.service;

import com.negri.minhasfinancas.model.entity.Projeto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProjetoService {

    Projeto salvar(Projeto projeto);

    Projeto atualizar(Projeto projeto);

    void deletar(Projeto projeto);

    List<Projeto> buscar(Projeto projetoFiltro );

    void validar(Projeto projeto);

    Optional<Projeto> obterPorId(Long id);
}
