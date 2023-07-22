package br.com.alura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.alura.dto.MensagemErroDto;
import br.com.alura.exception.BusinessException;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

	@Override
	public Response toResponse(BusinessException exception) {
		
		 List<String> mensagens = new ArrayList<>();
		 mensagens.add(exception.getMensagem());
		 return Response
	                .status(Response.Status.BAD_REQUEST)
	                .entity( MensagemErroDto.build(mensagens))
	                .build();
	}

}
