/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.web;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author 140200
 */
public class UsuarioBCJUnitTest extends BaseJUnitTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuarioBCJUnitTest.class);

    public UsuarioBCJUnitTest(String data) {
        super(data);
    }

    @Override
    public void tearDown() {
    }

    /**
     *
     */
    public void testAutenticarUserIdInvalido() {
        testContextLookupBC();

        UsuarioEntity user = new UsuarioEntity();
        user.setUserId("xxx");
        user.setSenha("yyy");
        try {
            user = getAutenticadorSC().autenticar(user);
            logger.info("Msg: " + user.getMsg().get(0));
        } catch (BCException ex) {
            assertTrue("Erro 'testAutenticarUserIdInvalido()'", false);
            logger.error(ex.getMessage());
        }
    }

    /**
     *
     */
    public void testIncluirUsuario() {
        testContextLookupBC();

        UsuarioEntity user = newUsuario();
        try {
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());
        } catch (BCException ex) {
            assertTrue("Erro 'testIncluirUsuario()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().excluir(user);
            } catch (BCException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testConsultarUsuario() {
        testContextLookupBC();

        UsuarioEntity user = newUsuario();

        try {
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getUserId().equals("sibinel"));

        } catch (BCException ex) {
            assertTrue("Erro 'testConsultarUsuario()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().excluir(user);
            } catch (BCException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testIncluirGrupo() {
        testContextLookupBC();

        GrupoEntity grupo = newGrupo();
        try {
            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());
        } catch (BCException ex) {
            assertTrue("Erro 'testIncluirUsuario()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().excluir(grupo);
            } catch (BCException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testConsultarGrupo() {
        testContextLookupBC();

        GrupoEntity grupo = newGrupo();

        try {
            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());

            grupo = getUsuarioBC().consultar(grupo);
            assertTrue(grupo != null);
            assertTrue(grupo.getNome().equals("grupo1"));

        } catch (BCException ex) {
            assertTrue("Erro 'testConsultarGrupo()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().excluir(grupo);
            } catch (BCException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testAssociarUsuarioGrupo() {
        testContextLookupBC();

        GrupoEntity grupo = newGrupo();
        UsuarioEntity user = newUsuario();
        Set<GrupoEntity> grupos = new HashSet<GrupoEntity>();

        try {
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());

            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());

            grupos.add(grupo);
            getUsuarioBC().associar(user, grupos);
            assertTrue(user.getMsg().isEmpty());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getUserId().equals("sibinel"));
            assertTrue(user.getGrupos().size() == 1);

        } catch (BCException ex) {
            assertTrue("Erro 'testAssociarUsuarioGrupo()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().desassociar(user, user.getGrupos());
                getUsuarioBC().excluir(user);
                getUsuarioBC().excluir(grupo);
            } catch (BCException exx) {
                logger.error(exx.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testDesassociarUsuarioGrupo() {
        testContextLookupBC();

        GrupoEntity grupo = newGrupo();
        UsuarioEntity user = newUsuario();
        Set<GrupoEntity> grupos = new HashSet<GrupoEntity>();

        try {
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());

            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());

            grupos.add(grupo);
            getUsuarioBC().associar(user, grupos);
            assertTrue(user.getMsg().isEmpty());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getUserId().equals("sibinel"));
            assertTrue(user.getGrupos().size() == 1);

            getUsuarioBC().desassociar(user, user.getGrupos());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getUserId().equals("sibinel"));
            assertTrue(user.getGrupos().isEmpty());

        } catch (BCException ex) {
            assertTrue("Erro 'testAssociarUsuarioGrupo()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().excluir(user);
                getUsuarioBC().excluir(grupo);
            } catch (BCException exx) {
                logger.error(exx.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testAutenticarUsuario() {
        testContextLookupBC();

        GrupoEntity grupo = newGrupo();
        UsuarioEntity user = newUsuario();
        FuncionalidadeEntity func = newFuncionalidade();

        Set<GrupoEntity> grupos = new HashSet<GrupoEntity>();
        Set<FuncionalidadeEntity> funcionalidades = new HashSet<FuncionalidadeEntity>();

        try {
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());

            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());

            grupos.add(grupo);
            getUsuarioBC().associar(user, grupos);
            assertTrue(user.getMsg().isEmpty());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getUserId().equals("sibinel"));
            assertTrue(user.getGrupos().size() == 1);

            grupo = user.getGrupos().iterator().next();

//            getSistemaBC().incluir(func);
            assertTrue(func.getMsg().isEmpty());

//            funcionalidades.add(getSistemaBC().consultar(func));
            getUsuarioBC().associar(grupo, funcionalidades);

            user = getAutenticadorSC().autenticar(user);
            assertTrue(user != null);
            assertTrue(user.getMsg().isEmpty());
            assertTrue(user.getUserId().equals("sibinel"));
            assertTrue(user.getGrupos().size() == 1);
            assertTrue(user.getGrupos().iterator().next().getFuncionalidades().size() == 1);
            assertTrue(user.getGrupos().iterator().next().getFuncionalidades().iterator().next().getNome().equals("func1"));

            grupo = user.getGrupos().iterator().next();
            funcionalidades = grupo.getFuncionalidades();

        } catch (BCException ex) {
            assertTrue("Erro 'testAssociarUsuarioGrupo()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().desassociar(grupo, funcionalidades);
                getUsuarioBC().desassociar(user, user.getGrupos());
//                getSistemaBC().excluir(func);
                getUsuarioBC().excluir(user);
                getUsuarioBC().excluir(grupo);
            } catch (BCException exx) {
                logger.error(exx.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testAssociarGrupoFuncionalidade() {
        testContextLookupBC();

        GrupoEntity grupo = newGrupo();
        UsuarioEntity user = newUsuario();
        FuncionalidadeEntity func = newFuncionalidade();

        Set<GrupoEntity> grupos = new HashSet<GrupoEntity>();
        Set<FuncionalidadeEntity> funcionalidades = new HashSet<FuncionalidadeEntity>();

        try {
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());

            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());

            grupos.add(grupo);
            getUsuarioBC().associar(user, grupos);
            assertTrue(user.getMsg().isEmpty());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getUserId().equals("sibinel"));
            assertTrue(user.getGrupos().size() == 1);

            grupo = user.getGrupos().iterator().next();

            func = newFuncionalidade();
//            getSistemaBC().incluir(func);
            assertTrue(func.getMsg().isEmpty());

//            funcionalidades.add(getSistemaBC().consultar(func));
            getUsuarioBC().associar(grupo, funcionalidades);

            grupo = getUsuarioBC().consultar(grupo);

            assertTrue(grupo != null);
            assertTrue(grupo.getNome().equals("grupo1"));
            assertTrue(grupo.getFuncionalidades().size() == 1);
            assertTrue(grupo.getFuncionalidades().iterator().next().getNome().equals("func1"));

        } catch (BCException ex) {
            assertTrue("Erro 'testAssociarUsuarioGrupo()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().desassociar(grupo, grupo.getFuncionalidades());
                getUsuarioBC().desassociar(user, user.getGrupos());
//                getSistemaBC().excluir(func);
                getUsuarioBC().excluir(user);
                getUsuarioBC().excluir(grupo);
            } catch (BCException exx) {
                logger.error(exx.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testDesassociarGrupoFuncionalidade() {
        testContextLookupBC();

        GrupoEntity grupo = newGrupo();
        UsuarioEntity user = newUsuario();
        FuncionalidadeEntity func = newFuncionalidade();

        Set<GrupoEntity> grupos = new HashSet<GrupoEntity>();
        Set<FuncionalidadeEntity> funcionalidades = new HashSet<FuncionalidadeEntity>();

        try {
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());

            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());

            grupos.add(grupo);
            getUsuarioBC().associar(user, grupos);
            assertTrue(user.getMsg().isEmpty());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getUserId().equals("sibinel"));
            assertTrue(user.getGrupos().size() == 1);

            grupo = user.getGrupos().iterator().next();

            func = newFuncionalidade();
//            getSistemaBC().incluir(func);
            assertTrue(func.getMsg().isEmpty());

//            funcionalidades.add(getSistemaBC().consultar(func));
            getUsuarioBC().associar(grupo, funcionalidades);

            grupo = getUsuarioBC().consultar(grupo);

            assertTrue(grupo != null);
            assertTrue(grupo.getNome().equals("grupo1"));
            assertTrue(grupo.getFuncionalidades().size() == 1);
            assertTrue(grupo.getFuncionalidades().iterator().next().getNome().equals("func1"));

            getUsuarioBC().desassociar(grupo, funcionalidades);

            grupo = getUsuarioBC().consultar(grupo);

            assertTrue(grupo != null);
            assertTrue(grupo.getNome().equals("grupo1"));
            assertTrue(grupo.getFuncionalidades().isEmpty());

        } catch (BCException ex) {
            assertTrue("Erro 'testAssociarUsuarioGrupo()'", false);
            logger.error(ex.getMessage());
        } finally {
            try {
                getUsuarioBC().desassociar(user, user.getGrupos());
//                getSistemaBC().excluir(func);
                getUsuarioBC().excluir(user);
                getUsuarioBC().excluir(grupo);
            } catch (BCException exx) {
                logger.error(exx.getMessage());
            }
        }
    }

    /**
     *
     */
    public void testAssociarPopsGrupoFuncionalidade() {
        testContextLookupBC();

        GrupoEntity grupo = newXGrupo();
        UsuarioEntity user = newXUsuario();
        FuncionalidadeEntity func = newXFuncionalidade();

        Set<GrupoEntity> grupos = new HashSet<GrupoEntity>();
        Set<FuncionalidadeEntity> funcionalidades = new HashSet<FuncionalidadeEntity>();

        try {
            user = getUsuarioBC().consultar(user);
            if (user.isOk()) {
                return;
            }

            user.getMsg().clear();
            getUsuarioBC().incluir(user);
            assertTrue(user.getMsg().isEmpty());

            getUsuarioBC().incluir(grupo);
            assertTrue(grupo.getMsg().isEmpty());

            grupos.add(grupo);
            getUsuarioBC().associar(user, grupos);
            assertTrue(user.getMsg().isEmpty());

            user = getUsuarioBC().consultar(user);
            assertTrue(user != null);
            assertTrue(user.getGrupos().size() == 1);

            grupo = user.getGrupos().iterator().next();

//            getSistemaBC().incluir(func);
            assertTrue(func.getMsg().isEmpty());

//            funcionalidades.add(getSistemaBC().consultar(func));
            getUsuarioBC().associar(grupo, funcionalidades);

            grupo = getUsuarioBC().consultar(grupo);
            assertTrue(grupo != null);
            assertTrue(grupo.getFuncionalidades().size() == 1);

        } catch (BCException ex) {
            assertTrue("Erro 'testAssociarUsuarioGrupo()'", false);
            logger.error(ex.getMessage());
        }
    }
}
