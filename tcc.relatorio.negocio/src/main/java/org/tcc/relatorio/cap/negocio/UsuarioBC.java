package org.tcc.relatorio.cap.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.dominio.util.IPolicy;
import org.tcc.relatorio.cap.dominio.util.PolicyOp;
import org.tcc.relatorio.cap.dominio.util.Sha1;
import org.tcc.relatorio.cap.persistencia.GrupoRepo;
import org.tcc.relatorio.cap.persistencia.FuncionalidadeRepo;
import org.tcc.relatorio.cap.persistencia.UsuarioRepo;
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.persistencia.EmpresaRepo;

/**
 *
 * @author 140200
 */
@Stateless
public class UsuarioBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioBC.class);

    @Inject
    @New
    private GrupoRepo grupoRepo;
    @Inject
    @New
    private UsuarioRepo usuarioRepo;
    @Inject
    @New
    private FuncionalidadeRepo funcionalidadeRepo;
    @Inject
    @New
    private EmpresaRepo empresaRepo;
    
    /**
     * Construtor.
     */
    public UsuarioBC() {
        LOGGER.debug("__| UsuarioBC");
    }

    /**
     * Incluir usuário.
     *
     * @param usuario Usuário para inclusão
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(UsuarioEntity usuario) throws BCException {

        try {
            if (usuario == null) {
                LOGGER.info("Usuário não informado");
                return;
            }
            if (usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                LOGGER.info("Login não informado");
                usuario.getMsg().add("Login não informado");
                return;
            }
            
            if (Validador.isMaior(usuario.getId(), 0)) {
                LOGGER.info("Id de usuário já cadastrado. id: "+usuario.getId());
                usuario.getMsg().add("Id de usuário já cadastrado. id: "+usuario.getId());
                return;
            }

            Set<UsuarioEntity> busca = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (!busca.isEmpty()) {
                usuario.getMsg().add("Usuário já cadastrado");
                LOGGER.info("Usuário {} já cadastrado", usuario.getUserId());
            } 
            
            List<UsuarioEntity> buscaEmail = usuarioRepo.listarPorEmail(usuario.getEmail());
            
            if (!buscaEmail.isEmpty()) {
                usuario.getMsg().add("Email já cadastrado");
                LOGGER.info("Email {} já cadastrado", usuario.getEmail());
            } 
            
            if(usuario.isOk()){
                for (IPolicy policy : usuario.getPolicies()) {
                    policy.apply(usuario, PolicyOp.INSERT);
                }

                if (usuario.isOk()) {
                    usuario.setSenha(Sha1.digest(usuario.getUserId()));

                    //   Este trecho é aonde é feita a amarração do Usuario com a Empresa:  Uma ou Todas
                    if (!Validador.isColecao(usuario.getEmpresas())) {
                        EmpresaEntity empresa = empresaRepo.buscarPorId(1L);
                        List<EmpresaEntity> listaInsti = new ArrayList<EmpresaEntity>();
                        listaInsti.add(empresa);
                        usuario.setEmpresas(listaInsti);
                    }
                    usuario.setFlExclusao(Confirmacao.NAO.getId());
                    usuarioRepo.persist(usuario);
                    LOGGER.debug("BC/ Usuario '{}' criado com sucesso", usuario.getUserId());
                }
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * Atualiza UsuarioEntity.
     *
     * @param usuario Usuario para atualização.
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(UsuarioEntity usuario) throws BCException {

        try {
            if (usuario == null) {
                LOGGER.info("Usuário não informado");
                return;
            }
            if (usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                LOGGER.info("Usuário não informado");
                usuario.getMsg().add("Usuário Id não informado");
                return;
            }

            List<UsuarioEntity> buscaEmail = usuarioRepo.listarPorEmail(usuario.getEmail());
            if (!buscaEmail.isEmpty() && buscaEmail.get(0).getId().longValue() != usuario.getId()) {
                usuario.getMsg().add("Email cadastrado para outro usuário.");
                LOGGER.info("Email cadastrado para outro usuário.{}", usuario.getEmail());
                return;
            } 

            Set<UsuarioEntity> busca = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (busca.isEmpty()) {
                usuario.getMsg().add("Usuário não cadastrado");
                LOGGER.info("Usuário {} nao cadastrado", usuario.getUserId());
            } else {

                UsuarioEntity entity = busca.iterator().next();

                entity.setRg(usuario.getRg());
                entity.setCpf(usuario.getCpf());
                entity.setSexo(usuario.getSexo());
                entity.setNome(usuario.getNome());
                entity.setEmail(usuario.getEmail());

                //entity.setSenha(usuario.getSenha());
                //entity.setSenha(Sha1.digest(usuario.getSenha()));
                entity.setDataNasc(usuario.getDataNasc());
                entity.setBloqueado(usuario.isBloqueado());
//                entity.setDataTermino(usuario.getDataTermino());
                entity.setDataExpSenha(usuario.getDataExpSenha());
                entity.setAcessoGeral(usuario.isAcessoGeral());

                List<EmpresaEntity> empresasTmp = new ArrayList<EmpresaEntity>();
                boolean isInstiUser = usuario.getEmpresas() != null;
                if (entity.getEmpresas() != null) {
                    for (EmpresaEntity instit : entity.getEmpresas()) {
                        if (isInstiUser && usuario.getEmpresas().contains(instit)) {
                            empresasTmp.add(instit);
                        }
                    }
                }
                entity.getEmpresas().clear();
                if (isInstiUser) {
                    entity.getEmpresas().addAll(empresasTmp);
                    for (EmpresaEntity instit : usuario.getEmpresas()) {
                        if (!entity.getEmpresas().contains(instit)) {
                            entity.getEmpresas().add(instit);
                        }
                    }
                }

                for (IPolicy policy : usuario.getPolicies()) {
                    policy.apply(usuario, PolicyOp.INSERT);
                }

                if (entity.isOk()) {
                    usuarioRepo.update(entity);
                    LOGGER.debug("BC/ Usuario '{}'Atualizado com sucesso", entity.getUserId());
                }
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public Set<UsuarioEntity> listar(UsuarioEntity usuario, List<Long> lstEmpresaId, UsuarioEntity usuarioLogado) throws BCException {

        try {
            if (usuario == null) {
                LOGGER.info("Usuário não informado");
                return null;
            }
            String userId = usuario.getUserId();
            if (Validador.isBlank(userId)) {
                userId = "%";
            }
            return usuarioRepo.listarPorUserIdLike(userId, lstEmpresaId, usuarioLogado);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * Consultar usuario atravez do UserId.
     *
     * @param usuario UsuarioEntity com userId.
     * @return Usuario.
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    public UsuarioEntity consultar(UsuarioEntity usuario) throws BCException {

        UsuarioEntity entity;

        try {
            if (usuario == null) {
                LOGGER.info("Usuário não informado");
                return null;
            }
            if (usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                LOGGER.info("Usuário não informado");
                usuario.getMsg().add("Usuário Id não informado");
                return null;
            }

            Set<UsuarioEntity> busca = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (busca.isEmpty()) {
                LOGGER.info("Usuário {} não encontrado", usuario.getUserId());
                usuario.getMsg().add("Usuário não encontrado");
                entity = usuario;
            } else {
                entity = busca.iterator().next();
            }
            return entity;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * Ecluir Usuario
     *
     * @param usuario Usuario para exclusão.
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void excluir(UsuarioEntity usuario) throws BCException {

        UsuarioEntity entity;

        try {
            if (usuario == null) {
                LOGGER.info("Usuário não informado");
                return;
            }
            if (usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                LOGGER.info("Usuário não informado");
                usuario.getMsg().add("Usuário Id não informado");
                return;
            }

            Set<UsuarioEntity> busca = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (busca.isEmpty()) {
                LOGGER.info("Usuário {} não encontrado", usuario.getUserId());
            } else {
                entity = busca.iterator().next();
                if(entity.getUserId().equals("admin")){
                    usuario.getMsg().add("Privilégios insuficientes para deletar o usuário \"ADMIN\".");
                }else{
                    entity.setFlExclusao(Confirmacao.SIM.getId());
                    usuarioRepo.update(entity);
                }
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * Incluir Grupo
     *
     * @param grupo Grupo para inclusão.
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(GrupoEntity grupo) throws BCException {

        try {
            if (grupo == null) {
                LOGGER.info("Grupo não informado");
                return;
            }
            if (grupo.getNome() == null) {
                LOGGER.info("Grupo não informado");
                grupo.getMsg().add("Nome Grupo não informado");
                return;
            }

            Set<GrupoEntity> busca = grupoRepo.listarPorNome(grupo.getNome());
            if (!busca.isEmpty()) {
                grupo.getMsg().add("Grupo já cadastrado");
                LOGGER.info("Grupo {} já cadastrado", grupo.getNome());
            } else {
                for (IPolicy policy : grupo.getPolicies()) {
                    policy.apply(grupo, PolicyOp.INSERT);
                }
                if (grupo.isOk()) {
                    grupo.setFlExclusao(Confirmacao.NAO.getId());
                    grupoRepo.persist(grupo);
                    LOGGER.debug("BC/ Grupo '{}'Criado com sucesso", grupo.getNome());
                }
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * atualizar Grupo
     *
     * @param grupo Grupo para atualizar.
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(GrupoEntity grupo) throws BCException {

        try {
            if (grupo == null) {
                LOGGER.info("Grupo não informado");
                return;
            }
            if (Validador.isBlank(grupo.getNome())) {
                LOGGER.info("Grupo não informado");
                grupo.getMsg().add("Preencha o nome do grupo.");
                return;
            }
            if (!Validador.isMaior(grupo.getId(), 0)) {
                LOGGER.info("Grupo não cadastrado.");
                grupo.getMsg().add("Grupo não cadastrado.");
                return;
            }

            GrupoEntity busca = grupoRepo.buscarPorId(grupo.getId());
            if (busca == null) {
                grupo.getMsg().add("Grupo não cadastrado");
                LOGGER.info("Grupo {} nao cadastrado", grupo.getNome());
            } else {
                GrupoEntity entity = busca;
                entity.setNome(grupo.getNome());
//                entity.setDataInicio(grupo.getDataInicio());
//                entity.setDataTermino(grupo.getDataTermino());
                entity.setDescricao(grupo.getDescricao());

                for (IPolicy policy : grupo.getPolicies()) {
                    policy.apply(grupo, PolicyOp.UPDATE);
                }
                if (grupo.isOk()) {
                    grupoRepo.update(entity);
                    LOGGER.debug("BC/ Grupo '{}' atualizado com sucesso", grupo.getNome());
                }
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * consultar Grupo
     *
     * @param grupo Grupo para consultar.
     * @return GrupoEntity grupo da consulta.
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    public GrupoEntity consultar(GrupoEntity grupo) throws BCException {

        GrupoEntity entity = null;

        try {
            if (grupo == null) {
                LOGGER.info("Grupo não informado");
                return null;
            }
            if (grupo.getNome() == null) {
                LOGGER.info("Grupo não informado");
                grupo.getMsg().add("Nome Grupo não informado");
                return null;
            }

            Set<GrupoEntity> busca = grupoRepo.listarPorNome(grupo.getNome());
            if (busca.isEmpty()) {
                LOGGER.info("Grupo {} não encontrado", grupo.getNome());
            } else {
                entity = busca.iterator().next();
            }
            return entity;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<GrupoEntity> listar(GrupoEntity grupo, UsuarioEntity usuarioLogado) throws BCException {
        try {
            if (grupo == null) {
                LOGGER.info("Grupo não informado");
                return null;
            }
            String nome = grupo.getNome();
            if (Validador.isBlank(nome)) {
                nome = "%";
            }

            return grupoRepo.listarPorNomeLike(nome, usuarioLogado);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * excluir Grupo
     *
     * @param grupo Grupo para excluir.
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void excluir(GrupoEntity grupo) throws BCException {

        GrupoEntity entity;

        try {
            if (grupo == null) {
                LOGGER.info("Grupo não informado");
                return;
            }
            if (grupo.getNome() == null) {
                LOGGER.info("Grupo não informado");
                grupo.getMsg().add("Nome Grupo não informado");
                return;
            }

            Set<GrupoEntity> busca = grupoRepo.listarPorNome(grupo.getNome());
            if (busca.isEmpty()) {
                LOGGER.info("Grupo {} não encontrado", grupo.getNome());
                grupo.getMsg().add("Grupo "+grupo.getNome()+" não encontrado");
            } else {
                entity = busca.iterator().next();
                if (!entity.getUsuarios().isEmpty()) {
                    grupo.getMsg().add("Grupo contem usuarios ativos. Impossível excluir");
                } else {
                    entity.setFlExclusao(Confirmacao.SIM.getId());
                    usuarioRepo.update(entity);
                    LOGGER.info("Grupo {} excluido", grupo.getNome());
                }
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * associar usuario a grupos.
     *
     * @param usuario Usuario par associar.
     * @param grupos Grupos para associar
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void associar(UsuarioEntity usuario, Set<GrupoEntity> grupos) throws BCException {

        List<String> grupoNomes = new ArrayList<String>();
        try {
            if (usuario == null) {
                LOGGER.info("Usuário não informado");
                return;
            }
            if (usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                LOGGER.info("Usuário não informado");
                usuario.getMsg().add("Usuário não informado");
                return;
            }

            for (GrupoEntity grupo : grupos) {
                if (grupo.getNome() == null && grupo.getId() == null) {
                    usuario.getMsg().add("Grupo invalido");
                }
                grupoNomes.add(grupo.getNome());
            }

            if (grupoNomes.isEmpty()) {
                usuario.getMsg().add("Lista de Grupos inválida");
            }

            Set<UsuarioEntity> busca = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (busca.isEmpty()) {
                usuario.getMsg().add("Usuario não cadastrado");
                LOGGER.info("Usuário {} não cadastrado", usuario.getUserId());
            }

            List<GrupoEntity> grupoBusca = grupoRepo.listarPorNomeLista(grupoNomes);
            if (grupoBusca.isEmpty() || grupoBusca.size() != grupos.size()) {
                usuario.getMsg().add("Lista de Grupos inválida");
            }

            if (usuario.isOk()) {
                usuario = busca.iterator().next();

                for (GrupoEntity grupo : grupoBusca) {
                    usuario.getGrupos().add(grupo);
                    grupo.getUsuarios().add(usuario);
                    LOGGER.debug("BC/ Usuario/Grupo Associados {}/{}", usuario.getUserId(), grupo.getNome());
                }
                usuarioRepo.update(usuario);
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * desassociar usuario a grupos.
     *
     * @param usuario Usuario par desassociar.
     * @param grupos Grupos para desassociar
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void desassociar(UsuarioEntity usuario, Set<GrupoEntity> grupos) throws BCException {

        List<String> grupoNomes = new ArrayList<String>();
        try {
            if (usuario == null) {
                LOGGER.info("Usuário não informado");
                return;
            }
            if (usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                LOGGER.info("Usuário não informado");
                usuario.getMsg().add("Usuário não informado");
                return;
            }

            for (GrupoEntity grupo : grupos) {
                if (grupo.getNome() == null && grupo.getId() == null) {
                    usuario.getMsg().add("Grupo invalido");
                }
                grupoNomes.add(grupo.getNome());
            }

            if (grupoNomes.isEmpty()) {
                usuario.getMsg().add("Lista de Grupos inválida");
            }

            Set<UsuarioEntity> busca = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (busca.isEmpty()) {
                usuario.getMsg().add("Usuario não cadastrado");
                LOGGER.info("Usuário {} não cadastrado", usuario.getUserId());
            }

            List<GrupoEntity> grupoBusca = grupoRepo.listarPorNomeLista(grupoNomes);
            if (grupoBusca.size() != grupos.size()) {
                usuario.getMsg().add("Lista de Grupos inválida");
            }

            if (usuario.isOk()) {
                usuario = busca.iterator().next();
                for (GrupoEntity grupo : grupoBusca) {
                    usuario.getGrupos().remove(grupo);
                    grupo.getUsuarios().remove(usuario);
                    grupoRepo.update(grupo);
                    LOGGER.debug("BC/ Usuario/Grupo Desassociados '{}'/{}", usuario.getUserId(), grupo.getNome());
                }
                usuarioRepo.update(usuario);
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * associar grupo funcionalidade.
     *
     * @param grupo Grupo para associar.
     * @param funcionalidaes Funcionalidades para associar.
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void associar(GrupoEntity grupo, Set<FuncionalidadeEntity> funcionalidaes) throws BCException {

        List<String> funcNomes = new ArrayList<String>();
        try {
            if (grupo == null) {
                LOGGER.info("Grupo não informado");
                return;
            }
            if (grupo.getNome() == null || grupo.getNome().isEmpty()) {
                LOGGER.info("Grupo não informado");
                grupo.getMsg().add("Grupo não informado");
                return;
            }

            for (FuncionalidadeEntity func : funcionalidaes) {
                if (func.getNome() == null && func.getId() == null) {
                    grupo.getMsg().add("Funcionalidade invalido");
                }
                funcNomes.add(func.getNome());
            }

            Set<GrupoEntity> busca = grupoRepo.listarPorNome(grupo.getNome());
            if (busca.isEmpty()) {
                grupo.getMsg().add("Grupo não cadastrado");
                LOGGER.info("Grupo {} não cadastrado", grupo.getNome());
            }

            List<FuncionalidadeEntity> funcLista = funcionalidadeRepo.listarFuncPorNomeLista(funcNomes);
            if (funcLista.isEmpty() || funcLista.size() != funcionalidaes.size()) {
                grupo.getMsg().add("Lista de Funcionalidades inválida");
            }

            if (grupo.isOk()) {
                grupo = busca.iterator().next();
                for (FuncionalidadeEntity func : funcLista) {
                    grupo.getFuncionalidades().add(func);
                    func.getGrupos().add(grupo);
                    funcionalidadeRepo.update(func);
                    LOGGER.info("BC/ Grupo/Func associados '{}'/{}", func.getNome(), func.getNome());
                }
                usuarioRepo.update(grupo);
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * desassociar grupo funcionalidade.
     *
     * @param grupo Grupo para desassociar.
     * @param funcionalidades Funcionalidades para desassociar.
     * @throws BCException Erro de acesso aos componentes de negocio.
     */
    @TransactionAttribute(REQUIRED)
    public void desassociar(GrupoEntity grupo, Set<FuncionalidadeEntity> funcionalidades) throws BCException {

        List<String> funcNomes = new ArrayList<String>();
        try {
            if (grupo == null) {
                LOGGER.info("Grupo não informado");
                return;
            }
            if (grupo.getNome() == null || grupo.getNome().isEmpty()) {
                LOGGER.info("Grupo não informado");
                grupo.getMsg().add("Grupo não informado");
                return;
            }

            for (FuncionalidadeEntity func : funcionalidades) {
                if (func.getNome() == null && func.getId() == null) {
                    grupo.getMsg().add("Funcionalidade invalido");
                }
                funcNomes.add(func.getNome());
            }

            if (funcNomes.isEmpty()) {
                grupo.getMsg().add("Lista de Funcionalidades vazia");
                LOGGER.info("Lista de Funcionalidades vazia");
            }

            Set<GrupoEntity> busca = grupoRepo.listarPorNome(grupo.getNome());
            if (busca.isEmpty()) {
                grupo.getMsg().add("Grupo não cadastrado");
                LOGGER.info("Grupo {} não cadastrado", grupo.getNome());
                return;
            }

            List<FuncionalidadeEntity> funcLista = funcionalidadeRepo.listarFuncPorNomeLista(funcNomes);
            if (funcLista.isEmpty() || funcLista.size() != funcionalidades.size()) {
                grupo.getMsg().add("Lista de Funcionalidades inválida");
            }

            if (grupo.isOk()) {
                grupo = busca.iterator().next();
                for (FuncionalidadeEntity func : funcLista) {
                    grupo.getFuncionalidades().remove(func);
                    func.getGrupos().remove(grupo);
                    funcionalidadeRepo.update(func);
                    LOGGER.info("BC/ Grupo/Func Desassociados '{}'/{}", func.getNome(), func.getNome());
                }
                usuarioRepo.update(grupo);
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /*
     * Altera a senha do usuário.
     * @param usuario Usuario para inclusão
     * @throws BCException Erro de processamento da solicitação (geralemnte acesso ao banco).
     */
    /**
     * Altera a senha do usuário. <br>Verificar a coleção de mensagens do objeto
     * usuario passado como parâmetro. Se não houver mensagens, a senha foi
     * alterada com sucesso.
     *
     * @param usuario Passar um objeto contendo o userId preenchido e a
     * propriedade senha contendo a senha atual (será consistida).
     * @param novaSenha Nova senha (aberta, ou seja, não codificada).
     * @throws BCException Retornada apenas em caso de erros de execução.
     */
    @TransactionAttribute(REQUIRED)
    public void alterarSenha(UsuarioEntity usuario, String novaSenha) throws BCException {
        try {
            // Verifica se o usuário foi informado
            if (usuario == null || usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                throw new BCException("Usuário não informado.");
            }
            // Verifica se a nova senha do usuário é permitida
            if (novaSenha == null || novaSenha.isEmpty()) {
                throw new BCException("A nova senha não pode ser vazia.");
            }
            // Busca o usuário
            Set<UsuarioEntity> usuarios = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (usuarios.isEmpty() || usuarios.size() > 1) {
                throw new BCException("Usuário inválido.");
            }
            // Compara as senhas
            UsuarioEntity usuarioBusca = usuarios.iterator().next();
            if (!usuarioBusca.getSenha().equals(Sha1.digest(usuario.getSenha()))) {
                throw new BCException("Senha incorreta.");
            }
            // Grava a nova senha
            usuarioBusca.setSenha(Sha1.digest(novaSenha));
            usuarioRepo.persist(usuarioBusca);
        } catch (BCException e) {
            usuario.getMsg().add(e.getMessage());
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

}
