package br.com.alura.interceptor;

import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ejb.EJBException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolationException;

import br.com.alura.resources.AgendamentoEmailResource;

@Interceptor
@Priority(1)
@br.com.alura.interceptor.Logger
public class LoggerInterceptor {
	
	@AroundInvoke
	public Object treatException(InvocationContext context) throws Exception {
		
		Logger logger = Logger.getLogger(context.getTarget().getClass().getName());
		
		try {
			return context.proceed();		
		}
		catch (Exception e) {
			
			if(e instanceof ConstraintViolationException) {
				logger.info(e.getMessage());
			}
			else {
				logger.severe(e.getMessage());
			}
			
			throw e;
		}		
		
	}
	

}
