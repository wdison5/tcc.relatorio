package org.tcc.relatorio.negocio;

import org.tcc.relatorio.enumeracao.Confirmacao;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.negocio.util.EntityPreencherUtil;
import org.tcc.relatorio.negocio.validator.ValidadorUtil;
import org.tcc.relatorio.persistencia.EmpenhoRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Eloy
 */
@Stateless
public class EmpenhoBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpenhoBC.class);

    @Inject
    @New
    private EmpenhoRepo empenhoRepo;
    @Inject
    @New
    private ValidadorUtil validador;

    private UsuarioEntity usuario;
    private EntityPreencherUtil preencherEntity;

    public EmpenhoBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    public List<PphEmpenhoEntity> listar(PphEmpenhoEntity empenhoInicial, PphEmpenhoEntity empenhoFinal) throws BCException {
        try {
            return (List) empenhoRepo.listar(empenhoInicial, empenhoFinal, new String[0]);
        } catch (DaoException e) {
            LOGGER.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

    @TransactionAttribute(REQUIRED)
    public void incluir(PphEmpenhoEntity empenho, UsuarioEntity usuarioLogado) throws BCException {
        LOGGER.info("empenho: {}", empenho);
        try {
            setUsuario(usuarioLogado);
            
            process(empenho, ValidadorUtil.INSERT);
            
            if (empenho.isOk()) {
                empenhoRepo.persist(empenho);
                LOGGER.debug("BC/ Empenho '{}' criado com sucesso", empenho.getId());
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    @TransactionAttribute(REQUIRED)
    public void excluir(PphEmpenhoEntity empenho, UsuarioEntity usuarioLogado) throws BCException {
        try{
            setUsuario(usuarioLogado);
            PphEmpenhoEntity entity = empenhoRepo.buscarPorId(empenho.getId());
            preencherEntity.set(entity);
            entity.setFlExclusao(Confirmacao.SIM.getId());
            empenhoRepo.update(entity);
        }catch(Exception e){
            empenho.getMsg().add("Erro ao exluir pagamento.");
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphEmpenhoEntity empenho, UsuarioEntity usuario) throws BCException {

        try {

            setUsuario(usuario);
            validador.valida(empenho, ValidadorUtil.UPDATE);

            if (empenho.isOk()) {

                PphEmpenhoEntity entity = empenhoRepo.buscarPorId(empenho.getId());

                if (entity == null) {
                    LOGGER.info("Empenho {} nao cadastrado", empenho.getId());
                } else {

                    //entity.setPphBeneficiario(empenho.getPphBeneficiario());
                    entity.setDtEmpenho(empenho.getDtEmpenho());
                    entity.setNrEmpenho(empenho.getNrEmpenho());
                    entity.setVlEmpenho(empenho.getVlEmpenho());
                    entity.setFlExclusao(empenho.getFlExclusao());

                    preencherEntity.set(entity);
                    empenhoRepo.update(entity);
                    LOGGER.debug("BC/ Empenho '{}'Alterado com sucesso", entity.getId());
                }
            }

        } catch (DaoException de) {
            LOGGER.error("BC/ error {}", de.getMessage());
            throw new BCException(de.getMessage());
        }
    }
    
    private void process(PphEmpenhoEntity entity, Integer tipo) throws DaoException, BCException {
        preencherEntity.set(entity);
        validador.valida(entity, tipo);
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
        preencherEntity = new EntityPreencherUtil(usuario);
    }

    public PphEmpenhoEntity buscarPorId(Long id) throws BCException {
        try {
            return empenhoRepo.buscarPorId(id);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphEmpenhoEntity> pesquisar(PphEmpenhoEntity empenhoInicial, PphEmpenhoEntity empenhoFinal, List<Long> idsUnidSaude, List<Long> idsUnidPagadora, UsuarioEntity usuarioLogado) throws BCException {
        try {
            return (List) empenhoRepo.pesquisar(empenhoInicial, empenhoFinal, idsUnidSaude, idsUnidPagadora, (usuarioLogado!=null?usuarioLogado.isAcessoGeral():false));
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

}
