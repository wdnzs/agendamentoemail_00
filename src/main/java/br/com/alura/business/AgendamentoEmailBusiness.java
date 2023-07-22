package br.com.alura.business;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import javax.validation.Valid;

import br.com.alura.dao.AgendamentoEmailDao;
import br.com.alura.entity.AgendamentoEmail;
import br.com.alura.exception.BusinessException;
import br.com.alura.interceptor.Logger;

@Stateless
@Logger
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AgendamentoEmailBusiness {
	
	@Resource(lookup = "java:jboss/mail/AgendamentoMailSession")
    private Session sessaoEmail;

	private static String EMAIL_FROM = "mail.address";
    private static String EMAIL_USER = "mail.smtp.user";
    private static String EMAIL_PASSWORD = "mail.smtp.pass";
	
	@Inject
	private AgendamentoEmailDao agendamentoEmailDao;
	
	public List<AgendamentoEmail> listarAgemndamentosEmail(){
	
		return agendamentoEmailDao.listarAgendamentosEmail();
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAgendamentoEmail( @Valid AgendamentoEmail agendamentoEmail) throws BusinessException {
		
		if(!agendamentoEmailDao
				.listarAgendamentosEmailPorEmail(agendamentoEmail.getEmail())
				.isEmpty()) {
			throw new BusinessException("Email j� est� agendado.");
			
		}
		
		agendamentoEmail.setEnviado(false);
		agendamentoEmailDao.salvarAgendamentoEmail(agendamentoEmail);
		
		
	}
	
	
	public void marcarEnviado(AgendamentoEmail agendamentoEmail) {
		agendamentoEmail.setEnviado(true);
		agendamentoEmailDao.alterarAgendamentoEmail(agendamentoEmail);
	}
	
	public List<AgendamentoEmail> listarAgendamentosEmailNaoEnviados(){
		
		return agendamentoEmailDao.listarAgendamentosEmailNaoEnviados();
	}
	
	public void enviarEmail( AgendamentoEmail agendamentoEmail) {
		
		 try {

	            MimeMessage mensagem = new MimeMessage(sessaoEmail);
	            mensagem.setFrom(sessaoEmail.getProperty(EMAIL_FROM));
	            mensagem.setRecipients(Message.RecipientType.TO, agendamentoEmail.getEmail());
	            mensagem.setSubject(agendamentoEmail.getAssunto());

	            mensagem.setText(Optional.ofNullable(agendamentoEmail.getMensagem()).orElse(""));

	            Transport.send(mensagem,
	                    sessaoEmail.getProperty(EMAIL_USER),
	                    sessaoEmail.getProperty(EMAIL_PASSWORD));

	        } catch (MessagingException e) {
	            throw new RuntimeException(e);
	        }
		
	}

}
