package br.com.alura.exception;

import javax.ejb.ApplicationException;


@ApplicationException(rollback=true)
public class BusinessException extends Exception{
	
	private String mensagem;
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(String mensagem) {
		
		super(mensagem);
		this.mensagem = mensagem;
		
	}

	public String getMensagem() {
		return mensagem;
	}
	
	

}
