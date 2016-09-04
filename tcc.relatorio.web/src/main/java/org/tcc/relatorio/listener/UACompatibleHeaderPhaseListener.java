package org.tcc.relatorio.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 * Inclui header na resposta do HTTP para forçar o IE8 ou superior a não 
 * utilizar modo de compatibilidade ao renderizar o documento (força renderizar
 * na versão mais atual), mesmo que a URL e/ou domínio estejam listados nas
 * opções de exibição de compatibilidade do navegador.
 * 
 * Fonte: http://stackoverflow.com/questions/6860779/how-to-make-a-meta-tag-the-first-one-in-the-head-section
 * 
 * @author jfascio
 */
public class UACompatibleHeaderPhaseListener implements PhaseListener  {
    private static final long serialVersionUID = 1L;

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        final FacesContext facesContext = event.getFacesContext();
        final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.addHeader("X-UA-Compatible", "IE=edge");
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }
}
