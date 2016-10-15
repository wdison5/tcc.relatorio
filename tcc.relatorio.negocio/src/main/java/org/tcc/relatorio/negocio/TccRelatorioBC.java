package org.tcc.relatorio.negocio;

import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.ProdutoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.persistencia.TccRelatorioRepo;

/**
 * @author Jose Wdison de Souza<wdison@hotmail.com>
 */
@Stateless
public class TccRelatorioBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(TccRelatorioBC.class);

    @Inject
    @New
    private TccRelatorioRepo relatorioRepo;
    
    private UsuarioEntity usuario;

    public TccRelatorioBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    public List<ProdutoEntity> pesquisar(ProdutoEntity produtoInicial, ProdutoEntity produtoFinal, UsuarioEntity usuarioLogado, Object ... idsTpProd) throws BCException {
        try {
            return (List) relatorioRepo.pesquisar(produtoInicial, produtoFinal, usuarioLogado, idsTpProd);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
}
