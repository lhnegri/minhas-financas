package com.negri.minhasfinancas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "projeto", schema = "financas")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "descricao_projeto")
    private String descricaoProjeto;

    @Column(name = "data_servico")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataServico;

    @Column(name = "valor")
    private BigDecimal valorServico;

    @Column(name = "data_inicio_pagamento")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataInicioPagamento;

    @Column(name = "numero_parcelas_pagamento")
    private Integer numeroParcelasPagamento;

    @Column(name = "data_inicio_recebimento")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataInicioRecebimento;

    @Column(name = "numero_parcelas_recebimento")
    private Integer numeroParcelasRecebimento;
}
