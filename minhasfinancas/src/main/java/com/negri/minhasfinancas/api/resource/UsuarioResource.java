package com.negri.minhasfinancas.api.resource;

import com.negri.minhasfinancas.api.dto.UsuarioDTO;
import com.negri.minhasfinancas.exception.ErroAutenticacaoException;
import com.negri.minhasfinancas.exception.RegraNegocioException;
import com.negri.minhasfinancas.model.entity.Usuario;
import com.negri.minhasfinancas.service.LancamentoService;
import com.negri.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

	private final UsuarioService service;
	private final LancamentoService lancamentoService;
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar( @RequestBody UsuarioDTO dto ) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch (ErroAutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto ) {
		
		Usuario usuario = Usuario.builder()
					.nome(dto.getNome())
					.email(dto.getEmail())
					.senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo( @PathVariable("id") Long id ) {
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity( HttpStatus.NOT_FOUND );
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}

}
