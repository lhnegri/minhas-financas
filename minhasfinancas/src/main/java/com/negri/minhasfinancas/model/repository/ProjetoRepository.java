package com.negri.minhasfinancas.model.repository;

import com.negri.minhasfinancas.model.entity.Lancamento;
import com.negri.minhasfinancas.model.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
