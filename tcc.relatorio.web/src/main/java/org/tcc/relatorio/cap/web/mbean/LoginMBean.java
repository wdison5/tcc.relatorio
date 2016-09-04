package org.tcc.relatorio.cap.web.mbean;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.RequestScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.negocio.InstituicaoBC;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@RequestScoped
@ManagedBean(name = "loginMBean")
public class LoginMBean implements Serializable {

    private static final long serialVersionUID = -117282570309362842L;
    private static final Logger logger = LoggerFactory.getLogger(LoginMBean.class);

    private static final String HOME_PAGE = "/view/home?faces-redirect=true";
    private static final String LOGIN_PAGE = "/public/login?faces-redirect=true";
    private static final String CHANGEPWD_PAGE = "/public/alterar-senha?faces-redirect=true";

    private String userId;
    private String senha;
    private String novaSenha1;
    private String novaSenha2;

    @EJB(name = "UsuarioBC")
    private UsuarioBC usr;
    @EJB(name = "InstituicaoBC")
    private InstituicaoBC instituicaoBC;

    /**
     * Construtor default.
     */
    public LoginMBean() {
        logger.warn("... LoginMBean()");
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNovaSenha1() {
        return novaSenha1;
    }

    public void setNovaSenha1(String novaSenha1) {
        this.novaSenha1 = novaSenha1;
    }

    public String getNovaSenha2() {
        return novaSenha2;
    }

    public void setNovaSenha2(String novaSenha2) {
        this.novaSenha2 = novaSenha2;
    }

    /**
     * *
     * Login String URL de retorno.
     *
     * @return String de redirect da pagina.
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            if (request.getUserPrincipal() != null) {
                logger.info("User: {} ja esta autendicado", userId);
            } else {
                logger.info("Login user: {}", userId);
                request.login(userId, senha);

                configurarSession(request);
            }
        } catch (ServletException e) {
            logger.info("Login Exception: {}", e.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválido(s).", null));
            return "login";
        }

        //you can fetch user from database for authenticated principal and do some action
        Principal principal = request.getUserPrincipal();
        logger.info("User: {} - autenticado.", principal.getName());

        return HOME_PAGE;
    }

    private void configurarSession(HttpServletRequest request) {
        // passou, então autenticou. Grava o nome extenso na sessão:
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUserId(userId);
        try {
            usuario = usr.consultar(usuario);
            List<InstituicaoEntity> instituicoes =  instituicaoBC.listComUSaudUPaga(userId);
            if (instituicoes != null && instituicoes.size() > 0) {
                List<Long> lstInstituicaoId = new ArrayList<Long>();
                List<Long> lstUnidSaudeId = new ArrayList<Long>();
                List<Long> lstUnidPagadoraId = new ArrayList<Long>();
                for (InstituicaoEntity instituicao : instituicoes) {
                    lstInstituicaoId.add(instituicao.getId());
                    
                    PphUnidSaudeEntity unidSaude = instituicao.getPphUnidSaude();
                    if (unidSaude != null) {
                        lstUnidSaudeId.add(unidSaude.getId());
                    }
                    PphUnidadePagadoraEntity unidadePagadora = instituicao.getPphUnidadePagadora();
                    if (unidadePagadora != null) {
                        lstUnidPagadoraId.add(unidadePagadora.getId());
                    }
                    
                    logger.debug(instituicao.getId() + ": " + instituicao.getTpUnid());
                }
                request.getSession().setAttribute("lstInstituicaoId", lstInstituicaoId);
                request.getSession().setAttribute("lstUnidSaudeId", lstUnidSaudeId);
                request.getSession().setAttribute("lstUnidPagadoraId", lstUnidPagadoraId);
                
                for (Long id : lstInstituicaoId ) logger.info("lstInstituicaoId : {}", id);
                for (Long id : lstUnidSaudeId   ) logger.info("lstUnidSaudeId   : {}", id);
                for (Long id : lstUnidPagadoraId) logger.info("lstUnidPagadoraId: {}", id);

                
            }
        } catch (BCException ex) {
            logger.error("Erro ao buscar o nome extenso do usuário: " + ex.getMessage() + " {}", ex);
            usuario.setNome(userId);
        }
        request.getSession().setAttribute("username", usuario.getNome());
        request.getSession().setAttribute("usuarioLogado", usuario);
    }

    /**
     * Logout
     *
     * @return String url de retorno
     */
    public Integer time() {
        FacesContext context = (FacesContext) FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = (HttpSession) request.getSession();
        int timeout = session.getMaxInactiveInterval();
        return timeout;
    }

    public String logout() {
        FacesContext context = (FacesContext) FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        logger.info("logout {}", request.getUserPrincipal());
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("session.invalidate() {}", request.getUserPrincipal());
        }
        return LOGIN_PAGE;
    }

    public String alterarSenha() throws BCException {
        FacesContext context = (FacesContext) FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUserId(request.getUserPrincipal().getName());

        // verifica se a nova senha confere
        if (!this.novaSenha1.equals(this.novaSenha2)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A nova senha é diferente de sua conferência. Verifique.", null));
            return null;
        }

        // Efetua a troca de senha
        usuario.setSenha(senha);
        usr.alterarSenha(usuario, novaSenha1);
        if (usuario.getMsg().size() > 0) { // se houver mensagens é porque deu erro.
            for (String msg : usuario.getMsg()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
            }
            return null;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "A senha foi alterada com sucesso.", null));
        //return HOME_PAGE;
        return null;
    }
}
