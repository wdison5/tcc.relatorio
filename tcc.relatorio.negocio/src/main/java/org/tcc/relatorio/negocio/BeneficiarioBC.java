package org.tcc.relatorio.negocio;

import org.tcc.relatorio.enumeracao.Confirmacao;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.dominio.PphAtestadoVidaEntity;
import org.tcc.relatorio.dominio.PphBancoEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphComunicacaoEntity;
import org.tcc.relatorio.dominio.PphContatoEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.dominio.PphEnderecoEntity;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.dominio.PphMunicipioEntity;
import org.tcc.relatorio.dominio.PphProcuradorEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.negocio.limpar.exluidos.Clonador;
import org.tcc.relatorio.negocio.util.EntityPreencherUtil;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.negocio.validator.ValidadorUtil;
import org.tcc.relatorio.persistencia.AgenciaBancariaRepo;
import org.tcc.relatorio.persistencia.BeneficiarioRepo;
import org.tcc.relatorio.persistencia.ComunicacaoRepo;
import org.tcc.relatorio.persistencia.EnderecoRepo;
import org.tcc.relatorio.persistencia.EstadoRepo;
import org.tcc.relatorio.persistencia.UnidSaudeRepo;
import org.tcc.relatorio.persistencia.MunicipioRepo;
import org.tcc.relatorio.persistencia.UnidadePagadoraRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author 140200
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BeneficiarioBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeneficiarioBC.class);

    @Inject
    @New
    private BeneficiarioRepo beneficiarioRepo;

    @Inject
    @New
    private ComunicacaoRepo comunicacaoRepo;
    @Inject
    @New
    private EnderecoRepo enderecoRepo;
    @Inject
    @New
    private MunicipioRepo municipioRepo;
    @Inject
    @New
    private EstadoRepo estadoRepo;
    @Inject
    @New
    private UnidadePagadoraRepo unidadePagadoraRepo;
    @Inject
    @New
    private UnidSaudeRepo unidSaudeRepo;
    @Inject
    @New
    private AgenciaBancariaRepo agenciaBancariaRepo;

    @Inject
    @New
    private ValidadorUtil validador;

    private UsuarioEntity usuario;
    private EntityPreencherUtil preencherEntity;

    /**
     * Construtor.
     */
    public BeneficiarioBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    @TransactionAttribute(REQUIRED)
    public void incluir(PphBeneficiarioEntity beneficiario, UsuarioEntity usuarioLogado) throws BCException {
        try {
            setUsuario(usuarioLogado);

            process(beneficiario, ValidadorUtil.INSERT);

            if (beneficiario.isOk()) {
                beneficiarioRepo.persist(beneficiario);
            }
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    private void process(PphBeneficiarioEntity beneficiario, Integer tipo) throws DaoException, BCException {
        boolean isNotBeneficiarioIniciao = beneficiario.getPphBeneficiario() == null || beneficiario.getPphBeneficiario().getId() == null || beneficiario.getPphBeneficiario().getId() < 1;

        preencherEntity.set(beneficiario);
        validador.valida(beneficiario, tipo);

        if (beneficiario.isDirty()) {
            return;
        }

//          ===>>> Busca tudo que precisa do banco <<<===
        //Set Endereco
        Set<PphEnderecoEntity> enderecoSet = beneficiario.getPphEnderecoSet();
        for (PphEnderecoEntity endereco : enderecoSet) {
            endereco.setPphMunicipio(municipioRepo.buscarPorId(endereco.getPphMunicipio().getId()));
        }
        //Unidade Pagadora
        PphUnidadePagadoraEntity unidadePagadora = beneficiario.getPphUnidadePagadora();
        unidadePagadora = unidadePagadoraRepo.buscarPorId(unidadePagadora.getId());
        //Unidade Saude/Instituicao
        PphUnidSaudeEntity unidSaude = beneficiario.getPphUnidSaude();
        if(unidSaude!=null && unidSaude.getId()!=null){
            unidSaude = unidSaudeRepo.getById(unidSaude.getId());
        }else{
            unidSaude = null;
        }
        //Beneficiario Inicial
        PphBeneficiarioEntity beneficiarioInicial = null;
        if (isNotBeneficiarioIniciao) {
            beneficiario.setPphBeneficiario(null);
        } else {
            beneficiarioInicial = beneficiarioRepo.buscarPorId(beneficiario.getPphBeneficiario().getId());
        }
        PphAgenciaBancariaEntity agenciaBancaria = null;
        if (beneficiario.getPphAgenciaBancaria() != null && beneficiario.getPphAgenciaBancaria().getId() != null && beneficiario.getPphAgenciaBancaria().getId() > 0) {
            agenciaBancaria = agenciaBancariaRepo.buscarPorId(beneficiario.getPphAgenciaBancaria().getId());
        }

        PphMunicipioEntity municipioProcurador = null;
        PphAgenciaBancariaEntity agenciaBancariaProcurador = null;
        Set<PphProcuradorEntity> procuradorSet = beneficiario.getPphProcuradorSet();
        if (procuradorSet != null && procuradorSet.size() > 0) {
            PphProcuradorEntity procurador = procuradorSet.iterator().next();
            if (procurador.getNmProcurador() != null && !procurador.getNmProcurador().trim().equals("")) {
                Set<PphEnderecoEntity> enderecoProcuradorSet = procurador.getPphEnderecoSet();
                if (enderecoProcuradorSet != null && enderecoProcuradorSet.size() > 0) {
                    PphEnderecoEntity enderecoProcurador = enderecoProcuradorSet.iterator().next();
                    if (enderecoProcurador.getDsEndereco() != null && !enderecoProcurador.getDsEndereco().trim().equals("")) {
                        municipioProcurador = enderecoProcurador.getPphMunicipio();
                        if (municipioProcurador != null && municipioProcurador.getId() != null && municipioProcurador.getId() > 0) {
                            municipioProcurador = municipioRepo.buscarPorId(municipioProcurador.getId());
                        }
                    }
                }
                agenciaBancariaProcurador = procurador.getPphAgenciaBancaria();
                if (agenciaBancariaProcurador != null && agenciaBancariaProcurador.getId() != null && agenciaBancariaProcurador.getId() > 0) {
                    agenciaBancariaProcurador = agenciaBancariaRepo.buscarPorId(agenciaBancariaProcurador.getId());
                }
            } else {
                beneficiario.setPphProcuradorSet(null);
            }
        } else {
            beneficiario.setPphProcuradorSet(null);
        }
//          ===>>> Setando todas as variaveis <<<===
        //Unidade Pagadora
        beneficiario.setPphUnidadePagadora(unidadePagadora);
        //Unidade Pagadora
        beneficiario.setPphUnidSaude(unidSaude);
        //Beneficiario Inicial
        if (!isNotBeneficiarioIniciao) {
            beneficiario.setPphBeneficiario(beneficiarioInicial);
            preencherEntity.set(beneficiarioInicial);
        }
        for (PphEnderecoEntity endereco : enderecoSet) {
            endereco.setPphBeneficiario(beneficiario);
            preencherEntity.set(endereco);
            validador.valida(endereco, tipo);
            beneficiario.getMsg().addAll(endereco.getMsg());
        }

        HashSet<PphComunicacaoEntity> comunicacaoSet = (HashSet<PphComunicacaoEntity>) beneficiario.getPphComunicacaoSet();
        trataComunicacao(comunicacaoSet, beneficiario, null, null);
        beneficiario.setPphComunicacaoSet(comunicacaoSet);

        Set<PphContatoEntity> contatoSet = beneficiario.getPphContatoSet();
        if (contatoSet != null && contatoSet.size() > 0) {
            PphContatoEntity contato = contatoSet.iterator().next();
            if (contato.getNmContato() != null && !contato.getNmContato().trim().equals("")) {
                contato.setPphBeneficiario(beneficiario);
                Set<PphComunicacaoEntity> comunicacaoContatoSet = contato.getPphComunicacaoSet();
                trataComunicacao(comunicacaoContatoSet, beneficiario, contato, null);
                contato.setPphComunicacaoSet(comunicacaoContatoSet);
                preencherEntity.set(contato);
            } else {
                beneficiario.setPphContatoSet(null);
            }
        } else {
            beneficiario.setPphContatoSet(null);
        }

        if (procuradorSet != null && procuradorSet.size() > 0) {
            PphProcuradorEntity procurador = procuradorSet.iterator().next();
            Set<PphComunicacaoEntity> comunicacaoProcuradorSet = procurador.getPphComunicacaoSet();
            trataComunicacao(comunicacaoProcuradorSet, beneficiario, null, procurador);
            if (!Validador.isBlank(procurador.getNmProcurador()) || comunicacaoProcuradorSet.size() > 0 || agenciaBancariaProcurador != null || !Validador.isBlank(procurador.getNrContaCorrente()) || (Validador.isColecao(procurador.getPphEnderecoSet()) && !Validador.isBlank(((PphEnderecoEntity) procurador.getPphEnderecoSet().toArray()[0]).getCdCep()))) {
                validador.valida(procurador, tipo);
                beneficiario.getMsg().addAll(procurador.getMsg());
                procurador.setPphBeneficiario(beneficiario);
                procurador.setPphComunicacaoSet(comunicacaoProcuradorSet);

                Set<PphEnderecoEntity> enderecoProcuradorSet = procurador.getPphEnderecoSet();
                if (enderecoProcuradorSet != null && enderecoProcuradorSet.size() > 0) {
                    PphEnderecoEntity enderecoProcurador = enderecoProcuradorSet.iterator().next();
                    validador.valida(enderecoProcurador, tipo);
                    if (!enderecoProcurador.isOk()) {
                        beneficiario.getMsg().add("Informações de endereço a baixo pertencem ao procurador.");
                        beneficiario.getMsg().addAll(enderecoProcurador.getMsg());
                    } else {
                        enderecoProcurador.setPphProcurador(procurador);
                        enderecoProcurador.setPphBeneficiario(beneficiario);
                        enderecoProcurador.setPphMunicipio(municipioProcurador);
                        preencherEntity.set(enderecoProcurador);
                    }
                }
                procurador.setPphAgenciaBancaria(agenciaBancariaProcurador);

                preencherEntity.set(procurador);
            } else {
                beneficiario.setPphProcuradorSet(null);
            }

        } else {
            beneficiario.setPphProcuradorSet(null);
        }
        beneficiario.setPphAgenciaBancaria(agenciaBancaria);
    }

    /**
     * Metodo trata a exclusão de itens de comunicação vazios.
     * <br>
     * A lista de Comunicacao é obrigatória.
     * <br>Para os demais parametros, deve setar apenas para quem o
     * Set<'PphComunicacaoEntity'> pertence
     *
     * @param comunicacaoSet
     * @param beneficiario
     * @param contato
     * @param procurador
     */
    private void trataComunicacao(Set<PphComunicacaoEntity> comunicacaoSet, PphBeneficiarioEntity beneficiario, PphContatoEntity contato, PphProcuradorEntity procurador) {
        PphComunicacaoEntity[] comunicacaoArray = new PphComunicacaoEntity[comunicacaoSet.size()];
        comunicacaoSet.toArray(comunicacaoArray);

        for (PphComunicacaoEntity comunicacao : comunicacaoArray) {
            comunicacaoSet.remove(comunicacao);
            if (comunicacao.getDsComunic() != null && !"".equals(comunicacao.getDsComunic().trim())) {
                comunicacao.setPphBeneficiario(beneficiario);
                comunicacao.setPphContato(contato);
                comunicacao.setPphProcurador(procurador);
                preencherEntity.set(comunicacao);
                comunicacaoSet.add(comunicacao);
            }
        }
    }

    @TransactionAttribute(REQUIRED)
    public void excluir(PphBeneficiarioEntity beneficiario, UsuarioEntity usuarioLogado) throws BCException {
        try{
            setUsuario(usuarioLogado);
            PphBeneficiarioEntity entity = beneficiarioRepo.buscarPorId(beneficiario.getId());
            preencherEntity.set(entity);
            entity.setFlExclusao(Confirmacao.SIM.getId());
            beneficiarioRepo.update(entity);
        }catch(Exception e){
            beneficiario.getMsg().add("Erro ao exluir beneficiario!");
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphBeneficiarioEntity beneficiario, UsuarioEntity usuarioLogado) throws BCException {
        try {
            setUsuario(usuarioLogado);
            
            process(beneficiario, ValidadorUtil.UPDATE);

            if (beneficiario.isOk()) {
                Clonador clonador = new Clonador();
                List<String> lstObjToClone = new ArrayList<String>();
                lstObjToClone.add("this");
                lstObjToClone.add("pphEnderecoSet");
                lstObjToClone.add("pphComunicacaoSet");
                lstObjToClone.add("pphContatoSet");
                lstObjToClone.add("pphContatoSet.pphComunicacaoSet");
                lstObjToClone.add("pphProcuradorSet");
                lstObjToClone.add("pphProcuradorSet.pphEnderecoSet");
                lstObjToClone.add("pphProcuradorSet.pphComunicacaoSet");
                PphBeneficiarioEntity entity = (PphBeneficiarioEntity) clonador.clonar(beneficiario, lstObjToClone);
                beneficiarioRepo.update(entity);
            }

        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphBeneficiarioEntity> pesquisaPorNome(String nomeBeneficiario, PphUnidSaudeEntity unidSaude) throws BCException {
        if ((nomeBeneficiario == null || "".equals(nomeBeneficiario)) && (unidSaude == null || unidSaude.getId() == null || unidSaude.getId() < 1)) {
            throw BCExceptionUtil.prepara(LOGGER, new Exception("Insira um parametro de pesquisa"));
        }
        try {
            return beneficiarioRepo.pesquisaPorNome(nomeBeneficiario, unidSaude);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphBeneficiarioEntity> pesquisaPorNome(String nomeBeneficiario, PphUnidadePagadoraEntity unidPagadora) throws BCException {
        if (Validador.isBlank(nomeBeneficiario) && (unidPagadora == null || unidPagadora.getId() == null || unidPagadora.getId() < 1)) {
            throw BCExceptionUtil.prepara(LOGGER, new Exception("Insira um parametro de pesquisa"));
        }
        try {
            return beneficiarioRepo.pesquisaPorNome(nomeBeneficiario, unidPagadora);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public PphBeneficiarioEntity buscarPorId(Long id) throws BCException {
        try {
            return beneficiarioRepo.buscarPorId(id);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphBeneficiarioEntity> listar(PphBeneficiarioEntity beneficiarioInicial, PphBeneficiarioEntity beneficiarioFinal) throws BCException {
        try {
            return (List) beneficiarioRepo.listar(beneficiarioInicial, beneficiarioFinal, new String[0]);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    public List<PphBeneficiarioEntity> pesquisar(PphBeneficiarioEntity beneficiarioEntity, List<Long> idsUnidSaude, List<Long> idsUnidPagadora, UsuarioEntity usuarioLogado) throws BCException {
        try {
            return (List) beneficiarioRepo.pesquisar(beneficiarioEntity, idsUnidSaude, idsUnidPagadora, (usuarioLogado!=null?usuarioLogado.isAcessoGeral():false));
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public BeneficiarioBC setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
        preencherEntity = new EntityPreencherUtil(usuario);
        return this;
    }

    public PphBeneficiarioEntity buscarPorIdParaDetalhar(Long id) throws BCException {
        PphBeneficiarioEntity item = null;
        try {
            item = beneficiarioRepo.buscarPorId(id);

            PphUnidSaudeEntity unidSaude = item.getPphUnidSaude();
            PphUnidadePagadoraEntity unidadePagadora = item.getPphUnidadePagadora();
            {
                if (item.getPphEnderecoSet() != null && item.getPphEnderecoSet().size() > 0) {
                    PphEnderecoEntity endereco = item.getPphEnderecoSet().iterator().next();
                    PphMunicipioEntity municipioBeneficiario = endereco.getPphMunicipio();
                    PphEstadoEntity estadoBeneficiario = municipioBeneficiario.getPphEstado();
                }
            }
            PphAgenciaBancariaEntity agenciaBancaria = item.getPphAgenciaBancaria();
            if (agenciaBancaria != null) {
                PphBancoEntity banco = agenciaBancaria.getPphBanco();
                PphMunicipioEntity municipioAgencia = agenciaBancaria.getPphMunicipio();
                PphEstadoEntity estadoAgencia = municipioAgencia.getPphEstado();
            }
            PphBeneficiarioEntity beneficiarioInicial = item.getPphBeneficiario();
            {
                int i = 0;
                Iterator<PphComunicacaoEntity> comuniItera = item.getPphComunicacaoSet().iterator();
                PphComunicacaoEntity[] comuniBenArray = new PphComunicacaoEntity[item.getPphComunicacaoSet().size() + 1];
                while (comuniItera.hasNext()) {
                    comuniBenArray[i++] = comuniItera.next();
                }
            }
            Iterator<PphContatoEntity> contatoIterator = item.getPphContatoSet().iterator();
            if (contatoIterator.hasNext()) {
                int i = 0;
                PphContatoEntity contato = contatoIterator.next();
                PphComunicacaoEntity[] comuniContArray = new PphComunicacaoEntity[contato.getPphComunicacaoSet().size() + 1];
                Iterator<PphComunicacaoEntity> comunContatoIterator = contato.getPphComunicacaoSet().iterator();
                while (comunContatoIterator.hasNext()) {
                    comuniContArray[i++] = comunContatoIterator.next();
                }
            }
            Iterator<PphProcuradorEntity> procuradorIterator = item.getPphProcuradorSet().iterator();
            if (procuradorIterator.hasNext()) {
                PphProcuradorEntity procurador = procuradorIterator.next();
                Iterator<PphEnderecoEntity> enderecoProcuradorIterator = procurador.getPphEnderecoSet().iterator();
                if (enderecoProcuradorIterator.hasNext()) {
                    PphEnderecoEntity enderecoProcurador = enderecoProcuradorIterator.next();
                    PphMunicipioEntity municipioProcurador = enderecoProcurador.getPphMunicipio();
                    PphEstadoEntity estadoProcurador = municipioProcurador.getPphEstado();
                }
                PphAgenciaBancariaEntity agenciaBancariaProcurador = procurador.getPphAgenciaBancaria();
                if (agenciaBancariaProcurador != null) {
                    PphBancoEntity bancoProcurador = agenciaBancariaProcurador.getPphBanco();
                }
                {
                    PphComunicacaoEntity[] comuniProcArray = new PphComunicacaoEntity[procurador.getPphComunicacaoSet().size() + 1];
                    int i = 0;
                    Iterator<PphComunicacaoEntity> comuniItera = procurador.getPphComunicacaoSet().iterator();
                    comuniProcArray = new PphComunicacaoEntity[3];
                    if (comuniItera.hasNext()) {
                        comuniProcArray[i++] = comuniItera.next();
                    }
                }
            }
            Set<PphAtestadoVidaEntity> atestadoVidaSet = item.getPphAtestadoVidaSet();
            if (Validador.isColecao(atestadoVidaSet)) {
                PphAtestadoVidaEntity atestado = atestadoVidaSet.iterator().next();
            }
            Set<PphEmpenhoEntity> emprenhoSet = item.getPphEmpenhoSet();
            if (Validador.isColecao(emprenhoSet)) {
                PphEmpenhoEntity empenho = emprenhoSet.iterator().next();
            }
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
        return item;
    }

}
