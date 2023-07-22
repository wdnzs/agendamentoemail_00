package br.com.alura.timer;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import br.com.alura.business.AgendamentoEmailBusiness;
import br.com.alura.entity.AgendamentoEmail;

@Singleton
public class AgendamentoEmailTimer {
	
	private static Logger logger = Logger.getLogger(AgendamentoEmailTimer.class.getName());
	
	@Inject
	private AgendamentoEmailBusiness agendamentoEmailBusiness;
	
	@Inject
	@JMSConnectionFactory( "java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;
	
	@Resource(mappedName = "java:/jms/queue/EmailQueue")
	private Queue queue;

	@Timeout
	public void timeout() {}
	
	@Schedule(hour="*",minute="*")
	public void enviarEmailsAgendados() {
		
		List<AgendamentoEmail> agendamenmentoEmails = agendamentoEmailBusiness.listarAgendamentosEmailNaoEnviados();
		
		agendamenmentoEmails
		.stream()
		.forEach(agendamenmentoEmail-> {
			context.createProducer().send(queue, agendamenmentoEmail);
			agendamentoEmailBusiness.marcarEnviado(agendamenmentoEmail);
		});
		
		
	}

}
