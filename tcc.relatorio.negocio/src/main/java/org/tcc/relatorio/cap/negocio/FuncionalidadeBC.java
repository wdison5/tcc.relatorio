/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.negocio;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.cap.dominio.util.IPolicy;
import org.tcc.relatorio.cap.dominio.util.PolicyOp;
import org.tcc.relatorio.cap.persistencia.FuncionalidadeRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author 140200
 */
@Stateless
public class FuncionalidadeBC implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FuncionalidadeBC.class);
    
    @Inject@New
    private FuncionalidadeRepo funcionalidadeRepo;

    /**
     * Construtor.
     */
    public FuncionalidadeBC() {
        logger.debug("__| "+this.getClass().getSimpleName());
    }

//    /**
//     *
//     * @param sistema Sistema para ser validado e incluido.
//     * @throws BCException Erro de processamento, geralmente acesso ao banco de dados.
//     */
//    @TransactionAttribute(REQUIRED)
//    public void incluir(SistemaEntity sistema) throws BCException {
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return;
//            }
//
//            Set<SistemaEntity> busca = sistemaRepo.listarPorNome(sistema.getNome());
//
//            if (!busca.isEmpty()) {
//                sistema.getMsg().add("Sistema já cadastrado");
//                logger.info("Sistema {} já cadastrado", sistema.getNome());
//            } else {
//                for (IPolicy policy : sistema.getPolicies()) {
//                    policy.apply(sistema, PolicyOp.INSERT);
//                }
//
//                if (sistema.isOk()) {
//                    sistemaRepo.persist(sistema);
//                    logger.debug("BC/ Sistema '{}' Criado com sucesso", sistema.getNome());
//                }
//            }
//        } catch (Exception e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }

//    /**
//     *
//     *
//     * @param sistema
//     * @throws BCException
//     */
//    @TransactionAttribute(REQUIRED)
//    public void atualizar(SistemaEntity sistema) throws BCException {
//
//        SistemaEntity entity;
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return;
//            }
//
//            Set<SistemaEntity> busca = sistemaRepo.listarPorNome(sistema.getNome());
//
//            if (busca.isEmpty()) {
//                sistema.getMsg().add("Sistema não cadastrado");
//                logger.info("Sistema {} não cadastrado", sistema.getNome());
//            } else {
//                entity = busca.iterator().next();
//                entity.setDataInicio(sistema.getDataInicio());
//                entity.setDataTermino(sistema.getDataTermino());
//                entity.setDescricao(sistema.getDescricao());
//
//                for (IPolicy policy : sistema.getPolicies()) {
//                    policy.apply(sistema, PolicyOp.UPDATE);
//                }
//
//                if (sistema.isOk()) {
//                    sistemaRepo.update(entity);
//                    logger.debug("BC/ Sistema '{}' atualizado com sucesso", sistema.getNome());
//                }
//            }
//        } catch (Exception e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }

//    /**
//     * 
//     * @param sistema
//     * @return
//     * @throws BCException 
//     */
//    public SistemaEntity consultar(SistemaEntity sistema) throws BCException {
//
//        SistemaEntity entity = null;
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return entity;
//            }
//
//            Set<SistemaEntity> busca = sistemaRepo.listarPorNome(sistema.getNome());
//            if (busca.isEmpty()) {
//                logger.info("Sistema {} não encontrado", sistema.getNome());
//            } else {
//                entity = busca.iterator().next();
//            }
//            return entity;
//        } catch (Exception e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }
//
//    /**
//     * 
//     * @param sistema
//     * @return
//     * @throws BCException 
//     */
//    public Set<SistemaEntity> listar(SistemaEntity sistema) throws BCException {
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return null;
//            }
//
//            return sistemaRepo.listarPorNomeLike(sistema.getNome());
//        } catch (Exception e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }
//
//    /**
//     * 
//     * @param sistema
//     * @throws BCException 
//     */
//    @TransactionAttribute(REQUIRED)
//    public void excluir(SistemaEntity sistema) throws BCException {
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema Id não informado");
//                return;
//            }
//
//            Set<SistemaEntity> busca = sistemaRepo.listarPorNome(sistema.getNome());
//            if (busca.isEmpty()) {
//                logger.info("Sistema {} não encontrado", sistema.getNome());
//                return;
//            }
//            SistemaEntity entity = busca.iterator().next();
//            if (!entity.getModulos().isEmpty()) {
//                sistema.getMsg().add("Sistema contem grupos ativos. Impossível excluir");
//            } else {
//                sistemaRepo.delete(entity);
//                logger.info("Sistema {} excluido", sistema.getNome());
//            }
//        } catch (Exception e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }
//
//    /**
//     *
//     * @param sistema
//     * @param modulo
//     * @return
//     * @throws BCException
//     */
//    public List<ModuloEntity> listarModulo(ModuloEntity modulo, SistemaEntity sistema) throws BCException {
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return null;
//            }
//            if (modulo == null || modulo.getNome() == null) {
//                modulo.getMsg().add("Modulo não informado");
//                return null;
//            }
//
//            List<ModuloEntity> busca = sistemaRepo.listarModuloPorSistemaNome(sistema, modulo.getNome());
//            return busca;
//        } catch (Exception e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }
//
//    /**
//     *
//     * @param sistema
//     * @param modulo
//     * @throws BCException
//     */
//    @TransactionAttribute(REQUIRED)
//    public void excluirModulo(ModuloEntity modulo, SistemaEntity sistema) throws BCException {
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return;
//            }
//
//            if (modulo == null || modulo.getNome() == null) {
//                modulo.getMsg().add("Modulo Id não informado");
//                return;
//            }
//
//            Set<SistemaEntity> lista = sistemaRepo.listarPorNome(sistema.getNome());
//            if (lista.isEmpty()) {
//                sistema.getMsg().add("Sistema não encontrado");
//                return;
//            }
//
//            sistema = lista.iterator().next();
//            List<ModuloEntity> busca = sistemaRepo.listarModuloPorSistemaNome(sistema, modulo.getNome());
//            if (busca.isEmpty()) {
//                logger.info("Modulo {} não encontrado", modulo.getNome());
//                return;
//            }
//
//            ModuloEntity entity = busca.get(0);
//            sistema.getModulos().remove(modulo);
//            sistemaRepo.update(sistema);
//            sistemaRepo.delete(entity);
//            logger.info("Modulo {} excluido", modulo.getNome());
//
//        } catch (Exception e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }
//
//    /**
//     *
//     * @param grupo
//     * @throws BCException
//     */
//    @TransactionAttribute(REQUIRED)
//    public void associar(SistemaEntity sistema, Set<ModuloEntity> modulos) throws BCException {
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return;
//            }
//
//            for (ModuloEntity modulo : modulos) {
//                if (modulo.getNome() == null) {
//                    sistema.getMsg().add("Modulo invalido");
//                    return;
//                }
//            }
//
//            Set<SistemaEntity> busca = sistemaRepo.listarPorNome(sistema.getNome());
//            if (busca.isEmpty()) {
//                sistema.getMsg().add("Sistema não cadastrado");
//                logger.info("Sistema {} não cadastrado", sistema.getNome());
//                return;
//            }
//
//            sistema = busca.iterator().next();
//
//            for (ModuloEntity modulo : modulos) {
//                if (!sistema.getModulos().contains(modulo)) {
//                    modulo.setSistema(sistema);
//                    sistemaRepo.persist(modulo);
//                    logger.info("Cadastrando Modulo {}", modulo.getNome());
//
//                    sistema.getModulos().add(modulo);
//                    logger.debug("BC/ Usuario/Grupo Associados '{}'/{}", sistema.getNome(), modulo.getNome());
//                }
//            }
//            sistemaRepo.update(sistema);
//        } catch (DaoException e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }

    /**
     *
     * @param func
     * @throws BCException
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(FuncionalidadeEntity func) throws BCException {

        try {
            if (func == null || func.getNome() == null) {
                func.getMsg().add("Funcionalidade não informado");
                return;
            }

            List<FuncionalidadeEntity> busca = funcionalidadeRepo.listarFuncPorNome(func.getNome());
            if (!busca.isEmpty()) {
                func.getMsg().add("Funcionalidade já cadastrada");
                logger.info("Funcionalidade {} já cadastrada", func.getNome());
            } else {
                for (IPolicy policy : func.getPolicies()) {
                    policy.apply(func, PolicyOp.UPDATE);
                }

                if (func.isOk()) {
                    funcionalidadeRepo.persist(func);
                    logger.debug("BC/ Funcionalidade '{}' criada com sucesso", func.getNome());
                }
            }
        } catch (Exception e) {
            logger.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

    /**
     *
     * @param func
     * @throws BCException
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(FuncionalidadeEntity func) throws BCException {

        try {
            if (func == null || func.getNome() == null) {
                func.getMsg().add("Funcionalidade não informado");
                return;
            }

            List<FuncionalidadeEntity> busca = funcionalidadeRepo.listarFuncPorNome(func.getNome());
            if (busca.isEmpty()) {
                func.getMsg().add("Funcionalidade não cadastrada");
                logger.info("Funcionalidade {} não cadastrada", func.getNome());
            } else {
                FuncionalidadeEntity entity = busca.get(0);

//                entity.setDataInicio(func.getDataInicio());
//                entity.setDataTermino(func.getDataTermino());
                entity.setDescricao(func.getDescricao());
                entity.setTipoOperacao(func.getTipoOperacao());
                entity.setUrl(func.getUrl());

                for (IPolicy policy : func.getPolicies()) {
                    policy.apply(func, PolicyOp.UPDATE);
                }

                if (func.isOk()) {
                    funcionalidadeRepo.update(func);
                    logger.debug("BC/ Funcionalidade '{}' atualizada com sucesso", func.getNome());
                }
            }
        } catch (Exception e) {
            logger.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

    /**
     *
     * @param func
     * @return
     * @throws BCException
     */
    public FuncionalidadeEntity consultar(FuncionalidadeEntity func) throws BCException {

        FuncionalidadeEntity entity = null;

        try {
            if (func == null || func.getNome() == null) {
                func.getMsg().add("Funcionalidade não informada");
                return entity;
            }

            List<FuncionalidadeEntity> busca = funcionalidadeRepo.listarFuncPorNome(func.getNome());
            if (busca.isEmpty()) {
                logger.info("Funcionalidade {} não encontrada", func.getNome());
            } else {
                entity = busca.get(0);
            }
            return entity;
        } catch (Exception e) {
            logger.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

    /**
     *
     * @param func
     * @return
     * @throws BCException 
     */
    public List<FuncionalidadeEntity> listar(FuncionalidadeEntity func) throws BCException {
        try {
            if (func == null || func.getNome() == null) {
                func.getMsg().add("Funcionalidade não informada");
                return null;
            }

            return funcionalidadeRepo.listarFuncPorNomeLike(func.getNome());
        } catch (Exception e) {
            logger.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

    /**
     *
     * @param func
     * @throws BCException
     */
    @TransactionAttribute(REQUIRED)
    public void excluir(FuncionalidadeEntity func) throws BCException {

        FuncionalidadeEntity entity;

        try {
            if (func == null || func.getNome() == null) {
                func.getMsg().add("Funcionalidade Id não informada");
                return;
            }

            List<FuncionalidadeEntity> busca = funcionalidadeRepo.listarFuncPorNome(func.getNome());
            if (busca.isEmpty()) {
                logger.info("Funcionalidade {} não encontrada", func.getNome());
                return;
            }

            entity = busca.get(0);
            funcionalidadeRepo.delete(entity);
            logger.info("Funcionalidade {} excluida", entity.getNome());

        } catch (Exception e) {
            logger.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

//    /**
//     *
//     * @param modulo
//     * @param funcionalidades
//     * @throws BCException
//     */
//    @TransactionAttribute(REQUIRED)
//    public void associar(SistemaEntity sistema, ModuloEntity modulo, Set<FuncionalidadeEntity> funcionalidades)
//            throws BCException {
//
//        List<String> listaNomes = new ArrayList<String>();
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return;
//            }
//
//            if (modulo == null || modulo.getNome() == null) {
//                sistema.getMsg().add("Modulo não informado");
//                return;
//            }
//
//            for (FuncionalidadeEntity func : funcionalidades) {
//                if (func.getNome() == null) {
//                    sistema.getMsg().add("Funcionalidade invalida");
//                    continue;
//                } else {
//                    listaNomes.add(func.getNome());
//                }
//            }
//
//            List<FuncionalidadeEntity> lista = sistemaRepo.listarFuncPorNomeLista(listaNomes);
//            if (lista.isEmpty() || lista.size() != funcionalidades.size()) {
//                sistema.getMsg().add("Funcionalidades não cadastrada: ");
//                logger.info("Funcionalidades não cadastradas");
//                return;
//            }
//
//            List<ModuloEntity> busca = sistemaRepo.listarModuloPorSistemaNome(sistema, modulo.getNome());
//            if (busca.isEmpty()) {
//                sistema.getMsg().add("Modulo não cadastrado");
//                logger.info("Modulo {} não cadastrado", sistema.getNome());
//                return;
//            }
//
//            modulo = busca.get(0);
//
//            for (FuncionalidadeEntity func : lista) {
//                if (!modulo.getFuncionalidades().contains(func)) {
//
////                    func.setModulo(modulo);
//                    sistemaRepo.update(func);
//
//                    modulo.getFuncionalidades().add(func);
//
//                    logger.debug("BC/ Sistema/Modulo/Func '{}'/{}/{} desacossiados", sistema.getNome(),
//                            modulo.getNome(), func.getNome());
//                }
//            }
//            sistemaRepo.update(modulo);
//        } catch (DaoException e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }
//
//    /**
//     *
//     * @param modulo
//     * @param funcionalidades
//     * @throws BCException
//     */
//    @TransactionAttribute(REQUIRED)
//    public void desassociar(SistemaEntity sistema, ModuloEntity modulo, Set<FuncionalidadeEntity> funcionalidades)
//        throws BCException {
//
//        List<String> listaNomes = new ArrayList<String>();
//
//        try {
//            if (sistema == null || sistema.getNome() == null) {
//                sistema.getMsg().add("Sistema não informado");
//                return;
//            }
//
//            if (modulo == null || modulo.getNome() == null) {
//                sistema.getMsg().add("Modulo não informado");
//                return;
//            }
//
//            for (FuncionalidadeEntity func : funcionalidades) {
//                if (func.getNome() == null) {
//                    sistema.getMsg().add("Funcionalidade invalida");
//                    continue;
//                } else {
//                    listaNomes.add(func.getNome());
//                }
//            }
//
//            List<FuncionalidadeEntity> lista = sistemaRepo.listarFuncPorNomeLista(listaNomes);
//            if (lista.isEmpty() || lista.size() != funcionalidades.size()) {
//                sistema.getMsg().add("Funcionalidades não cadastrada: ");
//                logger.info("Funcionalidades não cadastradas");
//                return;
//            }
//
//            List<ModuloEntity> busca = sistemaRepo.listarModuloPorSistemaNome(sistema, modulo.getNome());
//            if (busca.isEmpty()) {
//                sistema.getMsg().add("Modulo não cadastrado");
//                logger.info("Modulo {} não cadastrado", sistema.getNome());
//                return;
//            }
//
//            modulo = busca.get(0);
//
//            for (FuncionalidadeEntity func : lista) {
//                if (modulo.getFuncionalidades().contains(func)) {
//
//                    func.setModulo(null);
//                    sistemaRepo.update(func);
//
//                    modulo.getFuncionalidades().remove(func);
//                    logger.debug("BC/ Sistema/Modulo/Func '{}'/{}/{} desacossiados", sistema.getNome(),
//                            modulo.getNome(), func.getNome());
//                }
//            }
//            sistemaRepo.update(modulo);
//        } catch (DaoException e) {
//            logger.error("BC/ error {}", e.getMessage());
//            throw new BCException(e.getMessage());
//        }
//    }
}
