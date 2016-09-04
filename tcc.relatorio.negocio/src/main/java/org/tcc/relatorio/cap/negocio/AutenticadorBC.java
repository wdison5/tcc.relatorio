/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.negocio;

import java.util.Calendar;

import java.util.Date;
import java.util.Set;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.dominio.util.IPolicy;
import org.tcc.relatorio.cap.dominio.util.PolicyOp;
import org.tcc.relatorio.cap.negocio.service.AutenticadorSC;
import org.tcc.relatorio.cap.persistencia.UsuarioRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author 140200
 */
@Stateless
public class AutenticadorBC implements AutenticadorSC {

    private static final Logger logger = LoggerFactory.getLogger(AutenticadorBC.class);

    @Inject
    @New
    private UsuarioRepo usuarioRepo;

    @Resource
    private SessionContext ctx;

    /**
     * Construtor. 
     */
    public AutenticadorBC() {
        logger.debug("__| AutenticadorBC");
    }

    /**
     * Autenticar usuário.
     *
     * @param usuario Usuario para autenticação: UserId e Senha.
     * @return UsuarioEntity Informações do Usuario caso ele seja autenticado.
     * @throws BCException Erro de processamento (geralmente acesso ao banco de
     * dados.
     */
    @Override
    @PermitAll
    @TransactionAttribute(REQUIRED)
    public UsuarioEntity autenticar(UsuarioEntity usuario) throws BCException {

        UsuarioEntity usr = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        try {
            if (usuario == null || usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                logger.warn("usuario não informado");
                return null;
            }

            logger.debug("__| autenticar({})", usuario.getUserId());
            Set<UsuarioEntity> list = usuarioRepo.listarPorUserId(usuario.getUserId());
            if (list.isEmpty()) {
                usuario.getMsg().add("UserId não encontrado");
            } else if (list.size() > 1) {
                usuario.getMsg().add("UserId inconsistente. Mais de uma ocorrência cadastrada");
            } else {
                usr = list.iterator().next();
                if (usr.isBloqueado() || usr.getTentativas() >= 3) {
                    usuario.getMsg().add("UserId não autorizado");
                } else if (usr.getDataExpSenha() != null && usr.getDataExpSenha().before(cal.getTime())) {
                    usr.setSenhaExpirada(true);
                    cal.add(Calendar.MONTH, -1);
                    if (cal.getTime().after(usr.getDataExpSenha())) {
                        usuario.getMsg().add("Senha expirada");
                    }
                }

                //TODO incluir sha1
                if (!usr.getSenha().equals(usuario.getSenha())) {
                    usuario.getMsg().add("UserId/Password não compatível");
                }
            }

            if (usuario.isOk()) {
                return usr;
            } else {
                return null;
            }
        } catch (DaoException ex) {
            logger.error("ERROR: {}", ex);
            throw new BCException(ex);
        }
    }

    /**
     * Reinicia senha do usuario.
     *
     * @param usuario Usuario para reiniciar.
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @RolesAllowed("Authenticated")
    @TransactionAttribute(REQUIRED)
    public void reiniciar(UsuarioEntity usuario) throws BCException {

        try {
            if (usuario == null) {
                logger.info("Usuário não informado");
                return;
            }
            if (usuario.getUserId() == null || usuario.getUserId().isEmpty()) {
                logger.info("Usuário não informado");
                usuario.getMsg().add("Usuário Id não informado");
                return;
            }

            if (!usuario.getUserId().equals(ctx.getCallerPrincipal().getName())) {
                logger.info("Usuário informado '{}' não é o usuario logado '{}'", usuario.getUserId(),
                        ctx.getCallerPrincipal().getName());
                usuario.getMsg().add("Usuário informado não é o usuário logado");
                return;
            }

            Set<UsuarioEntity> busca = usuarioRepo.listarPorUserId(usuario.getUserId());

            if (busca.isEmpty()) {
                usuario.getMsg().add("Usuário não cadastrado");
                logger.info("Usuário {} não cadastrado", usuario.getUserId());
            } else {
                UsuarioEntity usr = busca.iterator().next();
                for (IPolicy policy : usuario.getPolicies()) {
                    policy.apply(usr, PolicyOp.RESET);
                }

                if (usr.isOk()) {
                    usuarioRepo.update(usr);
                    logger.debug("BC/ Usuario '{}' Reiniciado com sucesso", usr.getUserId());
                }
            }
        } catch (DaoException de) {
            logger.error("BC/ error {}", de.getMessage());
            throw new BCException(de.getMessage());
        } catch (Exception e) {
            logger.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }
}
