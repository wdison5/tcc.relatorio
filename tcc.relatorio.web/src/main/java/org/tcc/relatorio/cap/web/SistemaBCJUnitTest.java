/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.web;

import org.slf4j.LoggerFactory;
//import prodesp.cap.dominio.ModuloEntity;
//import prodesp.cap.dominio.SistemaEntity;

/**
 *
 * @author 140200
 */
public class SistemaBCJUnitTest extends BaseJUnitTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SistemaBCJUnitTest.class);

    public SistemaBCJUnitTest(String data) {
        super(data);
    }

//    /**
//     *
//     */
    public void testIncluirSistema() {
//        testContextLookupBC();
//
//        SistemaEntity sistema = newSistema();
//
//        try {
//            getSistemaBC().incluir(sistema);
//            assertTrue(sistema.getMsg().isEmpty());
//        } catch (BCException ex) {
//            assertTrue("Erro 'testIncluirSistema()'", false);
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                getSistemaBC().excluir(sistema);
//            } catch (BCException ex) {
//                logger.error(ex.getMessage());
//            }
//        }
    }
//
//    /**
//     *
//     */
    public void testConsultarSistema() {
//        testContextLookupBC();
//
//        SistemaEntity sistema = newSistema();
//
//        try {
//            getSistemaBC().incluir(sistema);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            sistema = getSistemaBC().consultar(sistema);
//            assertTrue(sistema != null);
//            assertTrue(sistema.getNome().equals("sistema1"));
//
//        } catch (BCException ex) {
//            assertTrue("Erro 'testConsultarSistema()'", false);
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                getSistemaBC().excluir(sistema);
//            } catch (BCException ex) {
//                logger.error(ex.getMessage());
//            }
//        }
    }

//    /**
//     *
//     */
    public void testAssociarSistemaModulo() {
//        testContextLookupBC();
//
//        ModuloEntity modulo = newModulo();
//        SistemaEntity sistema = newSistema();
//        Set<ModuloEntity> modulos = new HashSet<ModuloEntity>();
//
//        try {
//            getSistemaBC().incluir(sistema);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            modulos.add(modulo);
//            getSistemaBC().associar(sistema, modulos);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            sistema = getSistemaBC().consultar(sistema);
//            assertTrue(sistema != null);
//            assertTrue(sistema.getNome().equals("sistema1"));
//            assertTrue(sistema.getModulos().size() == 1);
//            modulo = sistema.getModulos().iterator().next();
//
//        } catch (BCException ex) {
//            assertTrue("Erro 'testAssociarSistemaModulo()'", false);
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                getSistemaBC().excluirModulo(modulo, sistema);
//                getSistemaBC().excluir(sistema);
//            } catch (BCException exx) {
//                logger.error(exx.getMessage());
//            }
//        }
    }
//
//    /**
//     * Test para incluir Funcionalidade.
//     */
    public void testIncluirFunc() {
//        testContextLookupBC();
//
//        FuncionalidadeEntity func = newFuncionalidade();
//
//        try {
//            getSistemaBC().incluir(func);
//            assertTrue(func.getMsg().isEmpty());
//        } catch (BCException ex) {
//            assertTrue("Erro 'testIncluirSistema()'", false);
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                getSistemaBC().excluir(func);
//            } catch (BCException ex) {
//                logger.error(ex.getMessage());
//            }
//        }
    }
//
//    /**
//     *
//     */
    public void testConsultarFunc() {
//        testContextLookupBC();
//
//        FuncionalidadeEntity func = newFuncionalidade();
//
//        try {
//            getSistemaBC().incluir(func);
//            assertTrue(func.getMsg().isEmpty());
//
//            func = getSistemaBC().consultar(func);
//            assertTrue(func != null);
//            assertTrue(func.getNome().equals("func1"));
//
//        } catch (BCException ex) {
//            assertTrue("Erro 'testConsultarSistema()'", false);
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                getSistemaBC().excluir(func);
//            } catch (BCException ex) {
//                logger.error(ex.getMessage());
//            }
//        }
    }
//
//    /**
//     *
//     */
    public void testAssociarSistemaModuloFuncionalidade() {
//        testContextLookupBC();
//
//        ModuloEntity modulo = newModulo();
//        SistemaEntity sistema = newSistema();
//        FuncionalidadeEntity func = newFuncionalidade();
//
//        Set<ModuloEntity> modulos = new HashSet<ModuloEntity>();
//        Set<FuncionalidadeEntity> funcs = new HashSet<FuncionalidadeEntity>();
//
//        try {
//            getSistemaBC().incluir(sistema);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            modulos.add(modulo);
//            getSistemaBC().associar(sistema, modulos);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            sistema = getSistemaBC().consultar(sistema);
//            assertTrue(sistema != null);
//            assertTrue(sistema.getNome().equals("sistema1"));
//            assertTrue(sistema.getModulos().size() == 1);
//
//            getSistemaBC().incluir(func);
//            assertTrue(func.getMsg().isEmpty());
//
//            func = getSistemaBC().consultar(func);
//            assertTrue(func != null);
//            assertTrue(func.getNome().equals("func1"));
//
//            funcs.add(func);
//            getSistemaBC().associar(sistema, modulo, funcs);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            sistema = getSistemaBC().consultar(sistema);
//            assertTrue(sistema != null);
//            assertTrue(sistema.getNome().equals("sistema1"));
//            assertTrue(sistema.getModulos().size() == 1);
//            assertTrue(sistema.getModulos().iterator().next().getFuncionalidades().size() == 1);
//
//        } catch (BCException ex) {
//            assertTrue("Erro 'testAssociarSistemaModulo()'", false);
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                getSistemaBC().excluir(func);
//                getSistemaBC().excluirModulo(modulo, sistema);
//                getSistemaBC().excluir(sistema);
//            } catch (BCException exx) {
//                logger.error(exx.getMessage());
//            }
//        }
    }
//
//    /**
//     *
//     */
    public void testDesassociarSistemaModuloFuncionalidade() {
//        testContextLookupBC();
//
//        ModuloEntity modulo = newModulo();
//        SistemaEntity sistema = newSistema();
//        FuncionalidadeEntity func = newFuncionalidade();
//
//        Set<ModuloEntity> modulos = new HashSet<ModuloEntity>();
//        Set<FuncionalidadeEntity> funcs = new HashSet<FuncionalidadeEntity>();
//
//        try {
//            getSistemaBC().incluir(sistema);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            modulos.add(modulo);
//            getSistemaBC().associar(sistema, modulos);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            sistema = getSistemaBC().consultar(sistema);
//            assertTrue(sistema != null);
//            assertTrue(sistema.getNome().equals("sistema1"));
//            assertTrue(sistema.getModulos().size() == 1);
//
//            getSistemaBC().incluir(func);
//            assertTrue(func.getMsg().isEmpty());
//
//            func = getSistemaBC().consultar(func);
//            assertTrue(func != null);
//            assertTrue(func.getNome().equals("func1"));
//
//            funcs.add(func);
//            getSistemaBC().associar(sistema, modulo, funcs);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            sistema = getSistemaBC().consultar(sistema);
//            assertTrue(sistema != null);
//            assertTrue(sistema.getNome().equals("sistema1"));
//            assertTrue(sistema.getModulos().size() == 1);
//            assertTrue(sistema.getModulos().iterator().next().getFuncionalidades().size() == 1);
//
//            func = getSistemaBC().consultar(func);
//            assertTrue(func != null);
//            assertTrue(func.getNome().equals("func1"));
//
//            funcs.clear();
//            funcs.add(func);
//            getSistemaBC().desassociar(sistema, modulo, funcs);
//            assertTrue(sistema.getMsg().isEmpty());
//
//            sistema = getSistemaBC().consultar(sistema);
//            assertTrue(sistema != null);
//            assertTrue(sistema.getNome().equals("sistema1"));
//            assertTrue(sistema.getModulos().size() == 1);
//            assertTrue(sistema.getModulos().iterator().next().getFuncionalidades().isEmpty());
//
//        } catch (BCException ex) {
//            assertTrue("Erro 'testAssociarSistemaModulo()'", false);
//            logger.error(ex.getMessage());
//        } finally {
//            try {
//                getSistemaBC().excluir(func);
//                getSistemaBC().excluirModulo(modulo, sistema);
//                getSistemaBC().excluir(sistema);
//            } catch (BCException exx) {
//                logger.error(exx.getMessage());
//            }
//        }
    }
}
