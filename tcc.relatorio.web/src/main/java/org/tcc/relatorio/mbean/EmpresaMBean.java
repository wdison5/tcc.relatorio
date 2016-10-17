package org.tcc.relatorio.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.negocio.EmpresaBC;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.Funcoes;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@SessionScoped
@ManagedBean(name = "empresaMBean")
public class EmpresaMBean extends BaseMBean<EmpresaEntity> implements Serializable {

    private static final long serialVersionUID = 118231614034054149L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaMBean.class);

    @EJB(name = "EmpresaBC")
    private EmpresaBC empresaBC;
    private UsuarioEntity usuarioLogado;

    /**
     * Construtor default.
     */
    public EmpresaMBean() {
        setParameterClass(EmpresaEntity.class);
        super.initialize("Empresa");
        LOGGER.debug(getDescricaoMBean());
    }

    @PostConstruct
    public void initialize() {
        try {
            usuarioLogado = (UsuarioEntity) FacesUtil.getInSession("usuarioLogado");
        } catch (Exception e) {
            LOGGER.error("Erro: {}", e);
            FacesUtil.put(e);
        }
    }

    @Override
    public List<EmpresaEntity> manter(ManterOp op) throws BCException {
        List<EmpresaEntity> result = null;
        switch (op) {
            case INCLUIR:
                getItem().setIdUserIncl(usuarioLogado.getId());
                getItem().setDhInclusao(new Date());

                empresaBC.incluir(getItem(), usuarioLogado);
                if (getItem().isOk()) {
                    List<Long> lstEmpresaId = (List<Long>) Funcoes.getSessionObject("lstEmpresaId");
                    if(lstEmpresaId==null){
                        lstEmpresaId = new ArrayList<>();
                        FacesUtil.addInSession("lstEmpresaId", lstEmpresaId);
                    }
                    (lstEmpresaId).add(getItem().getId());
                }
                break;

            case EXCLUIR:
                empresaBC.excluir(getItem(), usuarioLogado, (List<Long>) FacesUtil.getInSession("lstEmpresaId"));
                break;
            case ATUALIZAR:
                empresaBC.atualizar(getItem());
                break;

            case LISTAR:
                result = empresaBC.listPorIds_NomeECdLike((List<Long>) Funcoes.getSessionObject("lstEmpresaId"), getItem().getNmEmpresa(), getItem().getCdEmpresa());
        }
        return result;
    }

    public String labelInsituicao(EmpresaEntity institu) {
        String result = "";
        if (institu != null) {
            result = "Empresa(" + institu.getCdEmpresa() + ") - " + institu.getNmEmpresa();
        }
        return result;
    }
    
    public String tituloTela() {
        if (this.getComplementoRelatorio().equals("Analitico")) {
            return "Relatório Analítico";
        }
        if (this.getComplementoRelatorio().equals("Sintetico")) {
            return "Relatório Sintético";
        }
        if (this.getComplementoRelatorio().equals("SQLLog")) {
            return "LOG - Empresa";
        }
        return "Consulta Empresa";
    }

    @Override
    public String compoeFiltro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String compoePeriodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepararTelaInclusao() {}

    public void inicializaRelatorio(String complemento) {
        this.setEstadoDefault();
        super.setComplementoRelatorio(complemento);
    }

    public void setEstadoDefault() {
        super.initialize("Empresa");
        super.setComplementoRelatorio("");
    }

}
