package org.tcc.relatorio.cap.web;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import junit.framework.TestCase;
import org.slf4j.LoggerFactory;
//import prodesp.cap.negocio.SistemaBC;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.service.AutenticadorSC;

/**
 *
 * @author 140200
 */
public class BaseJUnitTest extends TestCase {

    private UsuarioBC usuarioBC;
    private AutenticadorSC autenticadorSC;
//    private SistemaBC sistemaBC;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseJUnitTest.class);

    public BaseJUnitTest(String testName) {
        super(testName);
    }

    /**
     *
     */
    @Override
    public void setUp() {
        Context ctx = null;
        try {
            ctx = new InitialContext();
            setUsuarioBC((UsuarioBC) ctx.lookup("java:app/jcap.negocio-1.0.99/UsuarioBC"));
            setAutenticadorSC((AutenticadorSC) ctx.lookup("java:app/jcap.negocio-1.0.99/AutenticadorBC"));
//            setSistemaBC((SistemaBC) ctx.lookup("java:app/jcap.negocio-1.0.99/SistemaBC"));
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                }
            }
        }
    }

    /**
     *
     */
    public void testContextLookupBC() {
        assertTrue(getUsuarioBC() != null);
        assertTrue(getAutenticadorSC() != null);
    }

    /**
     * @return the usuarioBC
     */
    public UsuarioBC getUsuarioBC() {
        return usuarioBC;
    }

    /**
     * @param usuarioBC the usuarioBC to set
     */
    public void setUsuarioBC(UsuarioBC usuarioBC) {
        this.usuarioBC = usuarioBC;
    }

    /**
     * @return the autenticadorBC
     */
    public AutenticadorSC getAutenticadorSC() {
        return autenticadorSC;
    }

    /**
     * @param autenticadorSC the autenticadorBC to set
     */
    public void setAutenticadorSC(AutenticadorSC autenticadorSC) {
        this.autenticadorSC = autenticadorSC;
    }

    protected UsuarioEntity newUsuario() {
        UsuarioEntity user = new UsuarioEntity();
        user.setUserId("sibinel");
        user.setSenha("sibinel");
        user.setBloqueado(false);
        user.setCpf("07961203826");
        user.setEmail("rsibinel@sp.gov.br");
        user.setNome("Rogerio Sibinel");
        user.setRg("17368309");
//        user.setDataInicio(new Date());
        return user;
    }

    protected GrupoEntity newGrupo() {
        GrupoEntity grupo = new GrupoEntity();
        grupo.setNome("grupo1");
        grupo.setDescricao("descricao grupo 1");
//        grupo.setDataInicio(new Date());
        return grupo;
    }

    protected FuncionalidadeEntity newFuncionalidade() {
        FuncionalidadeEntity func = new FuncionalidadeEntity();
        func.setNome("func1");
        func.setDescricao("descricao func 1");
//        func.setDataInicio(new Date());
        func.setTipoOperacao("RW");
        func.setUrl("/cap.web/manterSistema.jsf");

        return func;
    }

    protected UsuarioEntity newXUsuario() {
        UsuarioEntity user = new UsuarioEntity();
        user.setUserId("pops");
        user.setSenha("pops");
        user.setBloqueado(false);
        user.setCpf("07961903826");
        user.setEmail("pops@sp.gov.br");
        user.setNome("Pops Sibinel");
        user.setRg("17368399");
//        user.setDataInicio(new Date());
        return user;
    }

    protected GrupoEntity newXGrupo() {
        GrupoEntity grupo = new GrupoEntity();
        grupo.setNome("foo");
        grupo.setDescricao("descricao foo");
//        grupo.setDataInicio(new Date());
        return grupo;
    }

    protected FuncionalidadeEntity newXFuncionalidade() {
        FuncionalidadeEntity func = new FuncionalidadeEntity();
        func.setNome("bar");
        func.setDescricao("descricao bar");
//        func.setDataInicio(new Date());
        func.setTipoOperacao("RW");
        func.setUrl("/cap.web/pops.jsf");

        return func;
    }
}
