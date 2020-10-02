package com.negri.minhasfinancas.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoDTO {

    private Long id;
    private String nomeCliente;
    private String descricaoProjeto;
    private LocalDate dataServico;
    private BigDecimal valorServico;
    private LocalDate dataInicioPagamento;
    private Integer numeroParcelasPagamento;
    private LocalDate dataInicioRecebimento;
    private Integer numeroParcelasRecebimento;
}
