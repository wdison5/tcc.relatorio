package org.tcc.relatorio.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.negocio.BeneficiarioBC;

/**
 *
 * @author Jose Wdison
 */
@FacesConverter(value = "beneficiarioConverter")
@ManagedBean(name = "beneficiarioConverter")
@RequestScoped
public class BeneficiarioConverter implements Converter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeneficiarioConverter.class);
    
    @EJB(name = "BeneficiarioBC")
    private BeneficiarioBC beneficiarioBC;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        PphBeneficiarioEntity result = new PphBeneficiarioEntity();
        if (value != null) {
            try {
                result = beneficiarioBC.buscarPorId(Long.valueOf(value));
            } catch (Exception e) {
                MBeanExceptionUtil.log(LOGGER, e, "Erro na conversão do beneficiario: " + e.getMessage());
            }
            LOGGER.debug(value);
        }
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String result = "";
        if (value != null && value instanceof PphBeneficiarioEntity && ((PphBeneficiarioEntity) value).getId()!=null) {
            LOGGER.debug(value.toString());
            result = "" + ((PphBeneficiarioEntity) value).getId();
        }
        return result;
    }

}
