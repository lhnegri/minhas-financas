package com.negri.minhasfinancas.api.resource;

import com.negri.minhasfinancas.api.dto.LancamentoDTO;
import com.negri.minhasfinancas.api.dto.ProjetoDTO;
import com.negri.minhasfinancas.exception.RegraNegocioException;
import com.negri.minhasfinancas.model.entity.Lancamento;
import com.negri.minhasfinancas.model.entity.Projeto;
import com.negri.minhasfinancas.model.entity.Usuario;
import com.negri.minhasfinancas.model.enums.StatusLancamento;
import com.negri.minhasfinancas.model.enums.TipoLancamento;
import com.negri.minhasfinancas.service.ProjetoService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
public class ProjetoResource {

    private final ProjetoService service;

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value ="nomeCliente") String nomeCliente,
            @RequestParam(value = "descricaoProjeto") String descricaoProjeto){

        Projeto projetoFiltro = Projeto.builder()
                .nomeCliente(nomeCliente)
                .descricaoProjeto(descricaoProjeto).build();

        List<Projeto> projetos = service.buscar(projetoFiltro);

        return ResponseEntity.ok(projetos);
    }

    @GetMapping("{id}")
    public ResponseEntity obterProjeto( @PathVariable("id") Long id ) {
        return service.obterPorId(id)
                .map( projeto -> new ResponseEntity(converter(projeto), HttpStatus.OK) )
                .orElseGet( () -> new ResponseEntity(HttpStatus.NOT_FOUND) );
    }

    @PostMapping
    public ResponseEntity salvar( @RequestBody ProjetoDTO dto ) {
        try {
            Projeto entidade = converter(dto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody ProjetoDTO dto ) {
        return service.obterPorId(id).map( entity -> {
            try {
                Projeto projeto = converter(dto);
                projeto.setId(entity.getId());
                service.atualizar(projeto);
                return ResponseEntity.ok(projeto);
            }catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
                new ResponseEntity("Projeto não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar( @PathVariable("id") Long id ) {
        return service.obterPorId(id).map( entidade -> {
            service.deletar(entidade);
            return new ResponseEntity( HttpStatus.NO_CONTENT );
        }).orElseGet( () ->
                new ResponseEntity("Projeto não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }

    private Projeto converter(ProjetoDTO dto) {
        Projeto projeto = Projeto.builder()
                .id(dto.getId())
                .nomeCliente((dto.getNomeCliente()))
                .descricaoProjeto(dto.getDescricaoProjeto())
                .dataServico(dto.getDataServico())
                .valorServico(dto.getValorServico())
                .dataInicioPagamento(dto.getDataInicioPagamento())
                .numeroParcelasPagamento(dto.getNumeroParcelasPagamento())
                .dataInicioRecebimento(dto.getDataInicioRecebimento())
                .numeroParcelasRecebimento(dto.getNumeroParcelasRecebimento()).build();

        return projeto;
    }

    private ProjetoDTO converter(Projeto projeto) {
        ProjetoDTO dto = ProjetoDTO.builder()
                .id(projeto.getId())
                .nomeCliente((projeto.getNomeCliente()))
                .descricaoProjeto(projeto.getDescricaoProjeto())
                .dataServico(projeto.getDataServico())
                .valorServico(projeto.getValorServico())
                .dataInicioPagamento(projeto.getDataInicioPagamento())
                .numeroParcelasPagamento(projeto.getNumeroParcelasPagamento())
                .dataInicioRecebimento(projeto.getDataInicioRecebimento())
                .numeroParcelasRecebimento(projeto.getNumeroParcelasRecebimento()).build();

        return dto;
    }
}
