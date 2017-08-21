package com.mark.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.mark.api.model.Lancamento;
import com.mark.api.model.Pessoa;
import com.mark.api.repository.LancamentoRepository;
import com.mark.api.repository.PessoaRepository;
import com.mark.api.service.exception.PessoaInexistenteOuInativaException;



@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	


	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoPeloCodigo(codigo);
		
		if(!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())){
			validarPessoa(lancamento);
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		return lancamentoRepository.save(lancamentoSalvo);
	}

	
	
	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		
		if(lancamento.getPessoa().getCodigo() != null){
			pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		}
		
		if(pessoa == null || pessoa.isInativo() ){
			throw new PessoaInexistenteOuInativaException();
		}
	}



	private Lancamento buscarLancamentoPeloCodigo(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if (lancamentoSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return lancamentoSalvo;
	}


//Tratamento ao tentar salvar um lançamento cuja a pessoa é inexistente 
	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}
	
	private Lancamento buscarLancamentoExistente(Long codigo){
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if(lancamentoSalvo == null){
			throw new IllegalArgumentException();
		}
		
		return lancamentoSalvo;
	}
	
}